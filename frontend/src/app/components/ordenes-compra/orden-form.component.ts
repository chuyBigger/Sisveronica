import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { forkJoin } from 'rxjs';
import { OrdenCompraService } from '../../services/ordencompra.service';
import { ClienteService } from '../../services/cliente.service';
import { ContratoService } from '../../services/contrato.service';
import { ProductoService } from '../../services/producto.service';
import { EnumsService } from '../../services/enums.service';
import { NotaVentaService } from '../../services/notaventa.service';
import { DatosDetalleCliente } from '../../models/cliente.model';
import { DatosDetalleContrato } from '../../models/contrato.model';
import { DatosListarProductos } from '../../models/producto.model';
import { DatosDetalleNota } from '../../models/notaventa.model';

@Component({
  selector: 'app-orden-form',
  standalone: true,
  imports: [
    CommonModule, RouterModule, ReactiveFormsModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatDatepickerModule, MatNativeDateModule,
    MatCardModule, MatSnackBarModule, MatProgressSpinnerModule,
    MatTableModule, MatChipsModule,
  ],
  templateUrl: './orden-form.component.html',
  styleUrl: './orden-form.component.scss',
})
export class OrdenFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private ordenService = inject(OrdenCompraService);
  private clienteService = inject(ClienteService);
  private contratoService = inject(ContratoService);
  private productoService = inject(ProductoService);
  private enumsService = inject(EnumsService);
  private notaventaService = inject(NotaVentaService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);

  esEdicion = false;
  ordenId: string | null = null;
  cargando = false;
  clientes: DatosDetalleCliente[] = [];
  contratos: DatosDetalleContrato[] = [];
  productos: DatosListarProductos[] = [];
  partidas: string[] = [];
  modoConfirmacion = false;
  datosGuardados: any = null;
  modoGeneracion = false;
  notasGeneradas: DatosDetalleNota[] = [];
  generandoNota = false;
  diaGenerando = '';

  totalesPorDia: Record<string, number> = {};
  totalesPorFila: number[] = [];
  granTotal = 0;

  diasSemana = ['martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo', 'lunes'];
  diasLabels: Record<string, string> = {
    martes: 'Martes', miercoles: 'Miércoles', jueves: 'Jueves',
    viernes: 'Viernes', sabado: 'Sábado', domingo: 'Domingo', lunes: 'Lunes'
  };

  form: FormGroup = this.fb.group({
    cliente_id: ['', Validators.required],
    contrato_id: ['', Validators.required],
    partida: ['', Validators.required],
    fechaInicioSemana: ['', Validators.required],
    detalles: this.fb.array([]),
  });

  get detalles(): FormArray { return this.form.get('detalles') as FormArray; }

  ngOnInit(): void {
    forkJoin({
      clientes: this.clienteService.listar(),
      partidas: this.enumsService.getPartidas(),
    }).subscribe({
      next: ({ clientes, partidas }) => {
        this.clientes = clientes;
        this.partidas = partidas;
        const idParam = this.route.snapshot.paramMap.get('id');
        if (idParam) {
          this.esEdicion = true;
          this.ordenId = idParam;
          this.cargarOrden(idParam);
        }
      },
    });
  }

  onClienteChange(): void {
    const clienteId = this.form.get('cliente_id')?.value;
    this.contratos = [];
    this.form.patchValue({ contrato_id: '' });
    if (clienteId) {
      this.contratoService.listar().subscribe((res) => (this.contratos = res));
    }
    this.detalles.clear();
    this.recalcularTotales();
  }

  onPartidaChange(): void {
    const partida = this.form.get('partida')?.value;
    if (!partida) return;
    this.cargando = true;
    const obs = partida === 'GENERAL'
      ? this.productoService.listar(0, 200)
      : this.productoService.listarPorPartida(partida, 0, 200);
    obs.subscribe({
      next: (res) => {
        this.productos = res.content ?? [];
        this.generarDetallesDesdeProductos();
        this.cargando = false;
      },
      error: () => {
        this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  generarDetallesDesdeProductos(): void {
    this.detalles.clear();
    for (const p of this.productos) {
      this.detalles.push(this.crearDetalle(p.id, p.nombre));
    }
    this.recalcularTotales();
  }

  crearDetalle(productoId: string, productoNombre: string): FormGroup {
    return this.fb.group({
      producto_id: [productoId],
      producto_nombre: [productoNombre],
      lunes: [0], martes: [0], miercoles: [0],
      jueves: [0], viernes: [0], sabado: [0], domingo: [0],
    });
  }

  onCellChange(): void {
    this.recalcularTotales();
  }

  recalcularTotales(): void {
    const nuevosTotales: Record<string, number> = {};
    for (const dia of this.diasSemana) {
      nuevosTotales[dia] = this.detalles.controls.reduce(
        (sum, c) => sum + (Number(c.get(dia)?.value) || 0), 0
      );
    }
    this.totalesPorDia = nuevosTotales;
    this.totalesPorFila = this.detalles.controls.map((c) =>
      this.diasSemana.reduce((sum, d) => sum + (Number(c.get(d)?.value) || 0), 0)
    );
    this.granTotal = this.totalesPorFila.reduce((a, b) => a + b, 0);
  }

  cargarOrden(id: string): void {
    this.cargando = true;
    this.ordenService.buscarPorId(id).subscribe({
      next: (orden) => {
        const cliente = this.clientes.find((c) => c.nombre === orden.cliente);
        this.form.patchValue({
          cliente_id: cliente?.id ?? '',
          partida: orden.partida,
          fechaInicioSemana: new Date(orden.fechaInicioSemana),
        });

        forkJoin({
          contratos: this.contratoService.listar(),
          productos: orden.partida === 'GENERAL'
            ? this.productoService.listar(0, 200)
            : this.productoService.listarPorPartida(orden.partida, 0, 200),
        }).subscribe({
          next: ({ contratos, productos }) => {
            this.contratos = contratos;
            this.productos = productos.content ?? [];
            const contratoMatch = this.contratos.find((c) => c.contrato === orden.contrato);
            this.form.patchValue({ contrato_id: contratoMatch?.id ?? '' });
            this.detalles.clear();
            for (const d of orden.detalles) {
              const prod = this.productos.find((p) => p.id === d.producto);
              this.detalles.push(this.fb.group({
                producto_id: [d.producto ?? ''],
                producto_nombre: [prod?.nombre ?? d.producto],
                lunes: [d.lunes ?? 0], martes: [d.martes ?? 0],
                miercoles: [d.miercoles ?? 0], jueves: [d.jueves ?? 0],
                viernes: [d.viernes ?? 0], sabado: [d.sabado ?? 0],
                domingo: [d.domingo ?? 0],
              }));
            }
            this.cargando = false;
            this.recalcularTotales();
          },
          error: () => {
            this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 });
            this.cargando = false;
          },
        });
      },
      error: () => {
        this.snackBar.open('Error al cargar orden de compra', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) return;
    if (this.detalles.length === 0) {
      this.snackBar.open('Seleccione una partida con productos', 'Cerrar', { duration: 3000 });
      return;
    }
    const raw = this.form.value;
    const fecha = raw.fechaInicioSemana instanceof Date
      ? raw.fechaInicioSemana.toISOString().split('T')[0]
      : raw.fechaInicioSemana;
    const datos: any = {
      cliente_id: raw.cliente_id, contrato_id: raw.contrato_id,
      partida: raw.partida, fechaInicioSemana: fecha,
      detalles: raw.detalles.map((d: any) => ({
        fecha, producto: String(d.producto_id),
        lunes: d.lunes || undefined, martes: d.martes || undefined,
        miercoles: d.miercoles || undefined, jueves: d.jueves || undefined,
        viernes: d.viernes || undefined, sabado: d.sabado || undefined,
        domingo: d.domingo || undefined,
      })),
    };
    this.cargando = true;
    const request = this.esEdicion && this.ordenId
      ? this.ordenService.actualizar(this.ordenId, { partida: datos.partida, fechaInicioSemana: datos.fechaInicioSemana, detalles: datos.detalles })
      : this.ordenService.registrar(datos);
    request.subscribe({
      next: (res) => {
        this.cargando = false;
        this.snackBar.open('Orden de compra guardada', 'Cerrar', { duration: 2000 });
        this.router.navigate(['/ordenes-compra']);
      },
      error: () => {
        this.cargando = false;
        this.snackBar.open('Error al guardar orden de compra', 'Cerrar', { duration: 3000 });
      },
    });
  }

  getConfirmaTotalFila(index: number): number {
    if (!this.datosGuardados?.detalles?.[index]) return 0;
    const d = this.datosGuardados.detalles[index];
    return this.diasSemana.reduce((s, dia) => s + (d[dia] || 0), 0);
  }
  getConfirmaTotalCol(dia: string): number {
    if (!this.datosGuardados?.detalles) return 0;
    return this.datosGuardados.detalles.reduce((s: number, d: any) => s + (d[dia] || 0), 0);
  }
  getConfirmaGranTotal(): number {
    if (!this.datosGuardados?.detalles) return 0;
    return this.datosGuardados.detalles.reduce((s: number, d: any) => s + this.diasSemana.reduce((s2, dia) => s2 + (d[dia] || 0), 0), 0);
  }

  nuevaOrden(): void {
    this.modoConfirmacion = false;
    this.modoGeneracion = false;
    this.datosGuardados = null;
    this.notasGeneradas = [];
    this.form.reset();
    this.detalles.clear();
    this.recalcularTotales();
    this.router.navigate(['/ordenes-compra/nuevo']);
  }

  activarModoGeneracion(): void { this.modoGeneracion = true; this.notasGeneradas = []; }

  generarNota(dia: string): void {
    if (!this.datosGuardados?.id) return;
    if (!this.datosGuardados.detalles?.some((d: any) => d[dia] > 0)) {
      this.snackBar.open(`No hay productos para ${this.diasLabels[dia]}`, 'Cerrar', { duration: 3000 });
      return;
    }
    this.generandoNota = true;
    this.diaGenerando = dia;
    this.notaventaService.generarDesdeOrden(this.datosGuardados.id, dia).subscribe({
      next: (nota) => {
        this.notasGeneradas.push(nota);
        this.generandoNota = false;
        this.diaGenerando = '';
        this.snackBar.open(`Nota para ${this.diasLabels[dia]} - Folio #${nota.id?.substring(0, 8)}`, 'Cerrar', { duration: 3000 });
      },
      error: (err) => {
        this.generandoNota = false;
        this.diaGenerando = '';
        this.snackBar.open(err.error?.error || 'Error al generar nota', 'Cerrar', { duration: 3000 });
      },
    });
  }

  generarTodasLasNotas(): void {
    if (!this.datosGuardados?.id) return;
    const diasConProductos = this.diasSemana.filter(d => this.datosGuardados.detalles?.some((dd: any) => dd[d] > 0));
    if (diasConProductos.length === 0) {
      this.snackBar.open('No hay productos con cantidad', 'Cerrar', { duration: 3000 });
      return;
    }
    this.generandoNota = true;
    this.diaGenerando = 'todas';
    let completadas = 0;
    for (const dia of diasConProductos) {
      this.notaventaService.generarDesdeOrden(this.datosGuardados.id, dia).subscribe({
        next: (nota) => { this.notasGeneradas.push(nota); completadas++; if (completadas === diasConProductos.length) { this.generandoNota = false; this.diaGenerando = ''; this.snackBar.open(`${completadas} notas generadas`, 'Cerrar', { duration: 3000 }); } },
        error: () => { completadas++; if (completadas === diasConProductos.length) { this.generandoNota = false; this.diaGenerando = ''; } },
      });
    }
  }

  tieneProductosDia(dia: string): boolean { return this.datosGuardados?.detalles?.some((d: any) => d[dia] > 0) ?? false; }
  getNotaIdCorto(id: string): string { return id?.substring(0, 8) ?? ''; }
  cancelar(): void { this.router.navigate(['/ordenes-compra']); }
  volverLista(): void { this.router.navigate(['/ordenes-compra']); }
}
