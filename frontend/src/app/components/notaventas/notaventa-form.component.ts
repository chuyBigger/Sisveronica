import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NotaVentaService } from '../../services/notaventa.service';
import { ClienteService } from '../../services/cliente.service';
import { ProductoService } from '../../services/producto.service';
import { EnumsService } from '../../services/enums.service';
import { DatosDetalleCliente } from '../../models/cliente.model';
import { DatosListarProductos } from '../../models/producto.model';

@Component({
  selector: 'app-notaventa-form',
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
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './notaventa-form.component.html',
  styleUrl: './notaventa-form.component.scss',
})
export class NotaVentaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private notaventaService = inject(NotaVentaService);
  private clienteService = inject(ClienteService);
  private productoService = inject(ProductoService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);

  esEdicion = false;
  notaId: string | null = null;
  cargando = false;
  clientes: DatosDetalleCliente[] = [];
  productos: DatosListarProductos[] = [];
  partidas: string[] = [];

  form: FormGroup = this.fb.group({
    clienteId: ['', Validators.required],
    partida: ['', Validators.required],
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
      this.notaId = idParam;
      this.cargarNota(this.notaId);
    }
  }

  crearDetalle(productoId?: string, cantidad?: number): FormGroup {
    return this.fb.group({
      productoId: [productoId ?? '', Validators.required],
      cantidad: [cantidad ?? 1, [Validators.required, Validators.min(1)]],
    });
  }

  agregarDetalle(): void {
    this.detalles.push(this.crearDetalle());
  }

  eliminarDetalle(index: number): void {
    this.detalles.removeAt(index);
  }

  calcularTotal(): number {
    let total = 0;
    for (let i = 0; i < this.detalles.length; i++) {
      const detalle = this.detalles.at(i);
      const prodId = detalle.get('productoId')?.value;
      const cantidad = detalle.get('cantidad')?.value || 0;
      const prod = this.productos.find((p) => p.id === prodId);
      if (prod) {
        total += prod.precioVenta * cantidad;
      }
    }
    return total;
  }

  cargarNota(id: string): void {
    this.cargando = true;
    this.notaventaService.buscarPorId(id).subscribe({
      next: (nota) => {
        const cliente = this.clientes.find((c) => c.nombre === nota.cliente);
        this.form.patchValue({
          clienteId: cliente?.id ?? '',
          partida: nota.partida,
        });
        if (nota.detalles && nota.detalles.length > 0) {
          this.detalles.clear();
          for (const d of nota.detalles) {
            const prod = this.productos.find((p) => p.nombre === d.producto);
            this.detalles.push(this.crearDetalle(prod?.id, d.cantidad));
          }
        }
        this.cargando = false;
      },
      error: () => {
        this.snackBar.open('Error al cargar nota de venta', 'Cerrar', { duration: 3000 });
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
    const datos = {
      clienteId: raw.clienteId,
      partida: raw.partida,
      detalles: raw.detalles.map((d: any) => ({
        productoId: String(d.productoId),
        cantidad: Number(d.cantidad),
      })),
    };

    if (this.esEdicion && this.notaId) {
      const updateData = {
        partida: datos.partida,
        detalles: datos.detalles.map((d: any) => ({
          cantidad: d.cantidad,
          producto: String(d.productoId),
        })),
      };
      this.notaventaService.actualizar(this.notaId, updateData).subscribe({
        next: () => {
          this.snackBar.open('Nota de venta actualizada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/notaventas']);
        },
        error: () => this.snackBar.open('Error al actualizar nota de venta', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.notaventaService.registrar(datos).subscribe({
        next: () => {
          this.snackBar.open('Nota de venta creada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/notaventas']);
        },
        error: () => this.snackBar.open('Error al crear nota de venta', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/notaventas']);
  }
}
