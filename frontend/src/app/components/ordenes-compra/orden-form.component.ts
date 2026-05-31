import { Component, inject, OnInit } from '@angular/core';
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
import { OrdenCompraService } from '../../services/ordencompra.service';
import { ClienteService } from '../../services/cliente.service';
import { ContratoService } from '../../services/contrato.service';
import { ProductoService } from '../../services/producto.service';
import { EnumsService } from '../../services/enums.service';
import { DatosDetalleCliente } from '../../models/cliente.model';
import { DatosDetalleContrato } from '../../models/contrato.model';
import { DatosListarProductos } from '../../models/producto.model';

@Component({
  selector: 'app-orden-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTableModule,
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
  private snackBar = inject(MatSnackBar);

  esEdicion = false;
  ordenId: string | null = null;
  cargando = false;
  clientes: DatosDetalleCliente[] = [];
  contratos: DatosDetalleContrato[] = [];
  productos: DatosListarProductos[] = [];
  partidas: string[] = [];
  modoConfirmacion = false;
  datosGuardados: any = null;

  diasSemana = ['lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'];
  diasLabels: Record<string, string> = {
    lunes: 'Lun', martes: 'Mar', miercoles: 'Mié', jueves: 'Jue',
    viernes: 'Vie', sabado: 'Sáb', domingo: 'Dom'
  };

  form: FormGroup = this.fb.group({
    cliente_id: ['', Validators.required],
    contrato_id: ['', Validators.required],
    partida: ['', Validators.required],
    fechaInicioSemana: ['', Validators.required],
    detalles: this.fb.array([]),
  });

  get detalles(): FormArray {
    return this.form.get('detalles') as FormArray;
  }

  ngOnInit(): void {
    this.clienteService.listar().subscribe((res) => (this.clientes = res));
    this.enumsService.getPartidas().subscribe((res) => (this.partidas = res));

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.esEdicion = true;
      this.ordenId = idParam;
      this.cargarOrden(idParam);
    }
  }

  onClienteChange(): void {
    const clienteId = this.form.get('cliente_id')?.value;
    if (clienteId) {
      this.contratoService.listar().subscribe((res) => {
        this.contratos = res;
      });
    }
    this.detalles.clear();
  }

  onPartidaChange(): void {
    const partida = this.form.get('partida')?.value;
    if (!partida) return;
    this.cargando = true;
    this.productoService.listarPorPartida(partida, 0, 200).subscribe({
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
  }

  crearDetalle(productoId: string, productoNombre: string): FormGroup {
    return this.fb.group({
      producto_id: [productoId],
      producto_nombre: [productoNombre],
      lunes: [null],
      martes: [null],
      miercoles: [null],
      jueves: [null],
      viernes: [null],
      sabado: [null],
      domingo: [null],
    });
  }

  getTotales(dia: string): number {
    return this.detalles.controls.reduce((sum, c) => sum + (Number(c.get(dia)?.value) || 0), 0);
  }

  getTotalSemana(index: number): number {
    const c = this.detalles.at(index);
    return this.diasSemana.reduce((sum, d) => sum + (Number(c.get(d)?.value) || 0), 0);
  }

  getGranTotal(): number {
    return this.detalles.controls.reduce((sum, _, i) => sum + this.getTotalSemana(i), 0);
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
        this.onClienteChange();
        setTimeout(() => {
          const contratoMatch = this.contratos.find((c) => c.contrato === orden.contrato);
          this.form.patchValue({ contrato_id: contratoMatch?.id ?? '' });
          if (orden.detalles && orden.detalles.length > 0) {
            this.productoService.listarPorPartida(orden.partida, 0, 200).subscribe({
              next: (res) => {
                this.productos = res.content ?? [];
                this.detalles.clear();
                for (const d of orden.detalles) {
                  const prod = this.productos.find((p) => p.nombre === d.producto);
                  this.detalles.push(this.fb.group({
                    producto_id: [prod?.id ?? ''],
                    producto_nombre: [d.producto],
                    lunes: [d.lunes],
                    martes: [d.martes],
                    miercoles: [d.miercoles],
                    jueves: [d.jueves],
                    viernes: [d.viernes],
                    sabado: [d.sabado],
                    domingo: [d.domingo],
                  }));
                }
                this.cargando = false;
              },
            });
          } else {
            this.cargando = false;
          }
        }, 100);
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
      cliente_id: raw.cliente_id,
      contrato_id: raw.contrato_id,
      partida: raw.partida,
      fechaInicioSemana: fecha,
      detalles: raw.detalles.map((d: any) => ({
        fecha: fecha,
        producto: String(d.producto_id),
        lunes: d.lunes ? Number(d.lunes) : undefined,
        martes: d.martes ? Number(d.martes) : undefined,
        miercoles: d.miercoles ? Number(d.miercoles) : undefined,
        jueves: d.jueves ? Number(d.jueves) : undefined,
        viernes: d.viernes ? Number(d.viernes) : undefined,
        sabado: d.sabado ? Number(d.sabado) : undefined,
        domingo: d.domingo ? Number(d.domingo) : undefined,
      })),
    };

    this.cargando = true;

    const request = this.esEdicion && this.ordenId
      ? this.ordenService.actualizar(this.ordenId, {
          partida: datos.partida,
          fechaInicioSemana: datos.fechaInicioSemana,
          detalles: datos.detalles,
        })
      : this.ordenService.registrar(datos);

    request.subscribe({
      next: (res) => {
        this.cargando = false;
        this.modoConfirmacion = true;
        this.datosGuardados = {
          ...res,
          clienteNombre: this.clientes.find(c => c.id === raw.cliente_id)?.nombre,
          contratoNombre: this.contratos.find(c => c.id === raw.contrato_id)?.contrato,
        };
        this.snackBar.open('Orden de compra guardada', 'Cerrar', { duration: 3000 });
      },
      error: () => {
        this.cargando = false;
        this.snackBar.open('Error al guardar orden de compra', 'Cerrar', { duration: 3000 });
      },
    });
  }

  getConfirmaTotalFila(index: number): number {
    if (!this.datosGuardados?.detalles?.[index]) return 0;
    const detalle = this.datosGuardados.detalles[index];
    return this.diasSemana.reduce((s, d) => s + (detalle[d] || 0), 0);
  }

  getConfirmaTotalCol(dia: string): number {
    if (!this.datosGuardados?.detalles) return 0;
    return this.datosGuardados.detalles.reduce((s: number, d: any) => s + (d[dia] || 0), 0);
  }

  getConfirmaGranTotal(): number {
    if (!this.datosGuardados?.detalles) return 0;
    return this.datosGuardados.detalles.reduce((s: number, d: any) =>
      s + this.diasSemana.reduce((s2, dia) => s2 + (d[dia] || 0), 0), 0);
  }

  nuevaOrden(): void {
    this.modoConfirmacion = false;
    this.datosGuardados = null;
    this.form.reset();
    this.detalles.clear();
    this.router.navigate(['/ordenes-compra/nuevo']);
  }

  volverLista(): void {
    this.router.navigate(['/ordenes-compra']);
  }

  cancelar(): void {
    this.router.navigate(['/ordenes-compra']);
  }
}
