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
  ordenId: number | null = null;
  cargando = false;
  clientes: DatosDetalleCliente[] = [];
  contratos: DatosDetalleContrato[] = [];
  productos: DatosListarProductos[] = [];
  partidas: string[] = [];

  diasSemana = ['lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'];

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
    this.productoService.listar(0, 100).subscribe((res) => {
      this.productos = res.content ?? res;
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.esEdicion = true;
      this.ordenId = Number(idParam);
      this.cargarOrden(this.ordenId);
    }
  }

  onClienteChange(): void {
    const clienteId = this.form.get('cliente_id')?.value;
    if (clienteId) {
      this.contratoService.listar().subscribe((res) => {
        this.contratos = res;
      });
    }
  }

  crearDetalle(producto?: number): FormGroup {
    return this.fb.group({
      fecha: [''],
      producto: [producto ?? '', Validators.required],
      lunes: [null],
      martes: [null],
      miercoles: [null],
      jueves: [null],
      viernes: [null],
      sabado: [null],
      domingo: [null],
    });
  }

  agregarDetalle(): void {
    this.detalles.push(this.crearDetalle());
  }

  eliminarDetalle(index: number): void {
    this.detalles.removeAt(index);
  }

  cargarOrden(id: number): void {
    this.cargando = true;
    this.ordenService.buscarPorId(id).subscribe({
      next: (orden) => {
        const cliente = this.clientes.find((c) => c.nombre === orden.cliente);
        const contrato = this.contratos.find((c) => c.contrato === orden.contrato);
        this.form.patchValue({
          cliente_id: cliente?.id ?? '',
          contrato_id: contrato?.id ?? '',
          partida: orden.partida,
          fechaInicioSemana: new Date(orden.fechaInicioSemana),
        });
        this.onClienteChange();
        if (orden.detalles && orden.detalles.length > 0) {
          this.detalles.clear();
          for (const d of orden.detalles) {
            const prod = this.productos.find((p) => p.nombre === d.producto);
            this.detalles.push(this.fb.group({
              fecha: [d.fecha],
              producto: [prod?.id ?? '', Validators.required],
              lunes: [d.lunes],
              martes: [d.martes],
              miercoles: [d.miercoles],
              jueves: [d.jueves],
              viernes: [d.viernes],
              sabado: [d.sabado],
              domingo: [d.domingo],
            }));
          }
        }
        this.cargando = false;
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
      this.snackBar.open('Agregue al menos un detalle', 'Cerrar', { duration: 3000 });
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
        fecha: d.fecha || fecha,
        producto: String(d.producto),
        lunes: d.lunes ? Number(d.lunes) : undefined,
        martes: d.martes ? Number(d.martes) : undefined,
        miercoles: d.miercoles ? Number(d.miercoles) : undefined,
        jueves: d.jueves ? Number(d.jueves) : undefined,
        viernes: d.viernes ? Number(d.viernes) : undefined,
        sabado: d.sabado ? Number(d.sabado) : undefined,
        domingo: d.domingo ? Number(d.domingo) : undefined,
      })),
    };

    if (this.esEdicion && this.ordenId) {
      this.ordenService.actualizar(this.ordenId, {
        partida: datos.partida,
        fechaInicioSemana: datos.fechaInicioSemana,
        detalles: datos.detalles,
      }).subscribe({
        next: () => {
          this.snackBar.open('Orden de compra actualizada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/ordenes-compra']);
        },
        error: () => this.snackBar.open('Error al actualizar orden de compra', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.ordenService.registrar(datos).subscribe({
        next: () => {
          this.snackBar.open('Orden de compra creada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/ordenes-compra']);
        },
        error: () => this.snackBar.open('Error al crear orden de compra', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/ordenes-compra']);
  }
}
