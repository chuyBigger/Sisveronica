import { Component, Inject, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators,
} from '@angular/forms';
import { forkJoin } from 'rxjs';
import {
  MAT_DIALOG_DATA, MatDialogRef, MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NotaVentaService } from '../../services/notaventa.service';
import { ClienteService } from '../../services/cliente.service';
import { ProductoService } from '../../services/producto.service';
import { EnumsService } from '../../services/enums.service';
import { DatosDetalleCliente } from '../../models/cliente.model';
import { DatosListarProductos } from '../../models/producto.model';

export interface NotaVentaFormData {
  mode: 'create' | 'edit';
  notaId?: string;
}

@Component({
  selector: 'app-notaventa-form-dialog',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatDialogModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatSnackBarModule, MatProgressSpinnerModule,
    MatDividerModule, MatTooltipModule,
  ],
  templateUrl: './notaventa-form-dialog.component.html',
  styleUrl: './notaventa-form-dialog.component.scss',
})
export class NotaVentaFormDialogComponent implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<NotaVentaFormDialogComponent>);
  private snackBar = inject(MatSnackBar);
  private notaventaService = inject(NotaVentaService);
  private clienteService = inject(ClienteService);
  private productoService = inject(ProductoService);
  private enumsService = inject(EnumsService);
  private cdr = inject(ChangeDetectorRef);

  constructor(@Inject(MAT_DIALOG_DATA) public data: NotaVentaFormData) {}

  clientes: DatosDetalleCliente[] = [];
  todosProductos: DatosListarProductos[] = [];
  partidas: string[] = [];
  cargando = false;
  guardando = false;
  partidaAnterior = '';

  form: FormGroup = this.fb.group({
    clienteId: ['', Validators.required],
    partida: ['', Validators.required],
    detalles: this.fb.array([]),
  });

  get detalles(): FormArray { return this.form.get('detalles') as FormArray; }

  readonly negocio = {
    nombre: 'CARNICERÍA "LA VERÓNICA"',
    eslogan: 'La Mejor Calidad',
    fiscal: 'Jesus Manuel Romo Alba',
    rfc: 'R.F.C. ROAJ600629RQ5',
    domicilio: 'C. ALEGRIA #211 BARRIO DEL ENCINO C. P. 20240, Aguascalientes, Ags.',
  };

  get clienteNombre(): string {
    const id = this.form.get('clienteId')?.value;
    return this.clientes.find(c => c.id === id)?.nombre ?? '';
  }

  get folioDisplay(): string {
    return this.data.mode === 'create' ? '—' : 'Edición';
  }

  get productos(): DatosListarProductos[] {
    const partida = this.form.get('partida')?.value;
    if (!partida || partida === 'GENERAL') return this.todosProductos;
    return this.todosProductos.filter(p => p.partida === partida);
  }

  ngOnInit(): void {
    this.cargando = true;
    forkJoin({
      clientes: this.clienteService.listar(),
      partidas: this.enumsService.getPartidas(),
      productos: this.productoService.listar(0, 200),
    }).subscribe({
      next: ({ clientes, partidas, productos }) => {
        try {
          this.clientes = clientes;
          this.partidas = partidas;
          this.todosProductos = Array.isArray(productos) ? productos : (productos?.content ?? []);
          if (this.data.mode === 'edit' && this.data.notaId) {
            this.cargarNota(this.data.notaId);
          } else {
            this.cargando = false;
            this.cdr.detectChanges();
          }
        } catch (e) {
          this.snackBar.open('Error al procesar datos', 'Cerrar', { duration: 3000 });
          this.cargando = false;
          this.cdr.detectChanges();
        }
      },
      error: () => {
        this.snackBar.open('Error al cargar datos', 'Cerrar', { duration: 3000 });
        this.cargando = false;
        this.cdr.detectChanges();
      },
    });
  }

  cargarNota(id: string): void {
    this.notaventaService.buscarPorId(id).subscribe({
      next: (nota) => {
        try {
          const cliente = Array.isArray(this.clientes)
            ? this.clientes.find((c) => c.nombre === nota.cliente)
            : undefined;
          this.form.patchValue({
            clienteId: cliente?.id ?? '',
            partida: nota.partida,
          });
          this.partidaAnterior = nota.partida;
          this.detalles.clear();
          if (nota.detalles && nota.detalles.length > 0) {
            for (const d of nota.detalles) {
              const prod = Array.isArray(this.todosProductos)
                ? this.todosProductos.find((p) => p.nombre === d.producto)
                : undefined;
              this.detalles.push(this.crearDetalle(prod?.id, d.cantidad));
            }
          }
          this.cargando = false;
          this.cdr.detectChanges();
        } catch (e) {
          this.cargando = false;
          this.cdr.detectChanges();
          this.snackBar.open('Error al procesar nota', 'Cerrar', { duration: 3000 });
        }
      },
      error: () => {
        this.cargando = false;
        this.cdr.detectChanges();
        this.snackBar.open('Error al cargar nota', 'Cerrar', { duration: 3000 });
      },
    });
  }

  crearDetalle(productoId?: string, cantidad?: number): FormGroup {
    return this.fb.group({
      productoId: [productoId ?? '', Validators.required],
      cantidad: [cantidad ?? 1, [Validators.required, Validators.min(0.1)]],
    });
  }

  agregarDetalle(): void {
    this.detalles.push(this.crearDetalle());
  }

  eliminarDetalle(index: number): void {
    this.detalles.removeAt(index);
  }

  calcularSubTotal(cantidad: number, precio: number): number {
    return cantidad * precio;
  }

  getPrecio(productoId: string): number {
    const prod = this.todosProductos.find((p) => p.id === productoId);
    return prod?.precioVenta ?? 0;
  }

  getProductoNombre(productoId: string): string {
    const prod = this.todosProductos.find((p) => p.id === productoId);
    return prod?.nombre ?? '';
  }

  getDetalleSubTotal(index: number): number {
    const detalle = this.detalles.at(index);
    if (!detalle) return 0;
    const prodId = detalle.get('productoId')?.value;
    const cantidad = detalle.get('cantidad')?.value || 0;
    return this.calcularSubTotal(cantidad, this.getPrecio(prodId));
  }

  calcularTotal(): number {
    let total = 0;
    for (let i = 0; i < this.detalles.length; i++) {
      total += this.getDetalleSubTotal(i);
    }
    return total;
  }

  onPartidaChange(): void {
    if (this.data.mode === 'edit' && this.partidaAnterior) {
      const nuevaPartida = this.form.get('partida')?.value;
      if (nuevaPartida && nuevaPartida !== this.partidaAnterior) {
        const confirmacion = confirm('Si cambia la partida se borrarán todos los productos seleccionados. ¿Desea continuar?');
        if (confirmacion) {
          this.detalles.clear();
          this.partidaAnterior = nuevaPartida;
        } else {
          this.form.patchValue({ partida: this.partidaAnterior });
        }
      }
    }
  }

  guardar(): void {
    if (this.form.invalid) return;
    if (this.detalles.length === 0) {
      this.snackBar.open('Agregue al menos un detalle', 'Cerrar', { duration: 3000 });
      return;
    }
    this.guardando = true;
    const raw = this.form.value;
    const datos = {
      clienteId: raw.clienteId,
      partida: raw.partida,
      detalles: raw.detalles.map((d: any) => ({
        productoId: String(d.productoId),
        cantidad: Number(d.cantidad),
      })),
    };

    if (this.data.mode === 'edit' && this.data.notaId) {
      const updateData = {
        partida: datos.partida,
        detalles: datos.detalles.map((d: any) => ({
          cantidad: d.cantidad,
          producto: String(d.productoId),
        })),
      };
      this.notaventaService.actualizar(this.data.notaId, updateData).subscribe({
        next: () => {
          this.guardando = false;
          this.cdr.detectChanges();
          this.dialogRef.close('saved');
        },
        error: (err) => {
          this.guardando = false;
          this.cdr.detectChanges();
          this.snackBar.open(err.error?.error || 'Error al actualizar nota', 'Cerrar', { duration: 5000 });
        },
      });
    } else {
      this.notaventaService.registrar(datos).subscribe({
        next: () => {
          this.guardando = false;
          this.cdr.detectChanges();
          this.dialogRef.close('saved');
        },
        error: (err) => {
          this.guardando = false;
          this.cdr.detectChanges();
          this.snackBar.open(err.error?.error || 'Error al crear nota', 'Cerrar', { duration: 5000 });
        },
      });
    }
  }

  cancelar(): void {
    this.dialogRef.close();
  }
}
