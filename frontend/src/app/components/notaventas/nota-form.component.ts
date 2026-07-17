import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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
import { ExtraService } from '../../services/extra.service';
import { ClienteService } from '../../services/cliente.service';
import { ProductoService } from '../../services/producto.service';
import { EnumsService } from '../../services/enums.service';
import { DatosDetalleCliente } from '../../models/cliente.model';
import { DatosListarProductos } from '../../models/producto.model';

export interface NotaFormData {
  mode: 'create' | 'edit';
  type: 'notaventa' | 'extra';
  notaId?: string;
  extraId?: string;
  ordenCompraId?: string;
  partida?: string;
}

@Component({
  selector: 'app-nota-form',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, MatDialogModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatSnackBarModule, MatProgressSpinnerModule,
    MatDividerModule, MatTooltipModule,
  ],
  templateUrl: './nota-form.component.html',
  styleUrl: './nota-form.component.scss',
})
export class NotaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);
  private notaventaService = inject(NotaVentaService);
  private extraService = inject(ExtraService);
  private clienteService = inject(ClienteService);
  private productoService = inject(ProductoService);
  private enumsService = inject(EnumsService);
  private cdr = inject(ChangeDetectorRef);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private dialogRef = inject(MatDialogRef<NotaFormComponent>, { optional: true });
  private dialogData = inject(MAT_DIALOG_DATA, { optional: true }) as NotaFormData | null;

  data!: NotaFormData;

  readonly dias = ['lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'];
  readonly diasCorto = ['Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá', 'Do'];

  clientes: DatosDetalleCliente[] = [];
  todosProductos: DatosListarProductos[] = [];
  partidas: string[] = [];
  cargando = false;
  guardando = false;
  partidaAnterior = '';
  diaSeleccionado = '';

  form: FormGroup = this.fb.group({
    clienteId: [''],
    partida: [{ value: '', disabled: false }],
    detalles: this.fb.array([]),
  });

  get detalles(): FormArray { return this.form.get('detalles') as FormArray; }
  get isExtra(): boolean { return this.data?.type === 'extra'; }
  get esCreacion(): boolean { return this.data?.mode === 'create'; }

  readonly negocio = {
    nombre: 'CARNICERÍA "LA VERÓNICA"',
    eslogan: 'La Mejor Calidad',
    fiscal: 'Jesus Manuel Romo Alba',
    rfc: 'R.F.C. ROAJ600629RQ5',
    domicilio: 'C. ALEGRIA #211 BARRIO DEL ENCINO C. P. 20240, Aguascalientes, Ags.',
  };

  get productos(): DatosListarProductos[] {
    const partida = this.form.get('partida')?.value;
    if (!partida || partida === 'GENERAL') return this.todosProductos;
    return this.todosProductos.filter(p => p.partida === partida);
  }

  getTitle(): string {
    if (this.isExtra) return this.esCreacion ? 'Nuevo Extra' : 'Editar Extra';
    return this.esCreacion ? 'Nueva Nota de Venta' : 'Editar Nota de Venta';
  }

  getLabel(): string { return this.isExtra ? 'EXTRA' : 'NOTA'; }

  ngOnInit(): void {
    if (this.dialogData) {
      this.data = this.dialogData;
    } else {
      const routeId = this.route.snapshot.paramMap.get('id');
      const isNew = this.route.snapshot.url.some(s => s.path === 'nuevo');
      this.data = {
        mode: isNew ? 'create' : 'edit',
        type: 'notaventa',
        notaId: routeId || undefined,
      };
    }

    this.cargando = true;

    // Safety timeout - ensure loading never stays stuck
    const loadingTimeout = setTimeout(() => {
      if (this.cargando) {
        console.warn('NotaFormComponent: loading timeout, forcing cargando=false');
        this.cargando = false;
        this.cdr.detectChanges();
        this.snackBar.open('Tiempo de carga agotado', 'Cerrar', { duration: 3000 });
      }
    }, 10000);

    if (this.isExtra) {
      const partida = this.data.partida;
      if (!partida) {
        this.snackBar.open('Partida no especificada para el extra', 'Cerrar', { duration: 3000 });
        this.cargando = false;
        this.cdr.detectChanges();
        clearTimeout(loadingTimeout);
        return;
      }
      this.form.get('partida')?.setValue(partida);
      this.form.get('partida')?.disable();
      this.productoService.listarPorPartida(partida!, 0, 200).subscribe({
        next: (res) => {
          this.todosProductos = Array.isArray(res) ? res : (res?.content ?? []);
          this.cargando = false;
          this.cdr.detectChanges();
          clearTimeout(loadingTimeout);
        },
        error: () => {
          this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 });
          this.cargando = false;
          this.cdr.detectChanges();
          clearTimeout(loadingTimeout);
        },
      });
    } else {
      forkJoin({
        clientes: this.clienteService.listar(),
        partidas: this.enumsService.getPartidas(),
        productos: this.productoService.listar(0, 200),
      }).subscribe({
next: ({ clientes, partidas, productos }) => {
            this.clientes = clientes;
            this.partidas = partidas;
            this.todosProductos = Array.isArray(productos) ? productos : (productos?.content ?? []);
            if (!this.esCreacion && this.data.notaId) {
              this.cargarNota(this.data.notaId, loadingTimeout);
            } else {
              this.cargando = false;
              this.cdr.detectChanges();
              clearTimeout(loadingTimeout);
            }
          },
          error: () => {
            this.snackBar.open('Error al cargar datos', 'Cerrar', { duration: 3000 });
            this.cargando = false;
            this.cdr.detectChanges();
            clearTimeout(loadingTimeout);
          },
      });
    }
  }

  private cargarNota(id: string, loadingTimeout?: ReturnType<typeof setTimeout>): void {
    this.notaventaService.buscarPorId(id).subscribe({
      next: (nota) => {
        const cliente = this.clientes.find(c => c.nombre === nota.cliente);
        this.form.patchValue({
          clienteId: cliente?.id ?? '',
          partida: nota.partida,
        });
        this.partidaAnterior = nota.partida;
        this.detalles.clear();
        if (nota.detalles?.length) {
          for (const d of nota.detalles) {
            const prod = this.todosProductos.find(p => p.nombre === d.producto);
            this.detalles.push(this.crearDetalle(prod?.id, d.cantidad));
          }
        }
        this.cargando = false;
        this.cdr.detectChanges();
        if (loadingTimeout) clearTimeout(loadingTimeout);
      },
      error: () => {
        this.snackBar.open('Error al cargar nota', 'Cerrar', { duration: 3000 });
        this.cargando = false;
        this.cdr.detectChanges();
        if (loadingTimeout) clearTimeout(loadingTimeout);
      },
    });
  }

  private crearDetalle(productoId?: string, cantidad?: number): FormGroup {
    return this.fb.group({
      productoId: [productoId ?? '', Validators.required],
      cantidad: [cantidad ?? 1, [Validators.required, Validators.min(0.1)]],
    });
  }

  agregarDetalle(): void { this.detalles.push(this.crearDetalle()); }

  eliminarDetalle(index: number): void { this.detalles.removeAt(index); }

  getPrecio(productoId: string): number {
    return this.todosProductos.find(p => p.id === productoId)?.precioVenta ?? 0;
  }

  getDetalleSubTotal(index: number): number {
    const detalle = this.detalles.at(index);
    if (!detalle) return 0;
    return (detalle.get('cantidad')?.value || 0) * this.getPrecio(detalle.get('productoId')?.value);
  }

  calcularTotal(): number {
    let total = 0;
    for (let i = 0; i < this.detalles.length; i++) {
      total += this.getDetalleSubTotal(i);
    }
    return total;
  }

  onPartidaChange(): void {
    if (this.data.mode !== 'edit' || !this.partidaAnterior) return;
    const nueva = this.form.get('partida')?.value;
    if (nueva && nueva !== this.partidaAnterior) {
      if (confirm('Si cambia la partida se borrarán todos los productos. ¿Desea continuar?')) {
        this.detalles.clear();
        this.partidaAnterior = nueva;
      } else {
        this.form.patchValue({ partida: this.partidaAnterior });
      }
    }
  }

  guardar(): void {
    if (this.form.invalid) return;
    if (this.detalles.length === 0) {
      this.snackBar.open('Agregue al menos un detalle', 'Cerrar', { duration: 3000 });
      return;
    }

    if (this.isExtra && !this.diaSeleccionado) {
      this.snackBar.open('Seleccione un día', 'Cerrar', { duration: 3000 });
      return;
    }

    this.guardando = true;
    const raw = this.form.value;

    if (this.isExtra) {
      const datos = {
        ordenCompraId: this.data.ordenCompraId!,
        dia: this.diaSeleccionado,
        detalles: raw.detalles.map((d: any) => ({
          productoId: String(d.productoId),
          cantidad: Number(d.cantidad),
        })),
      };
      this.extraService.crear(datos).subscribe({
        next: () => this.onSaved('Extra creado'),
        error: (err) => this.onError(err, 'Error al crear extra'),
      });
    } else {
      const datos = {
        clienteId: raw.clienteId,
        partida: raw.partida,
        detalles: raw.detalles.map((d: any) => ({
          productoId: String(d.productoId),
          cantidad: Number(d.cantidad),
        })),
      };

      if (this.data.mode === 'edit' && this.data.notaId) {
        this.notaventaService.actualizar(this.data.notaId, {
          partida: datos.partida,
          detalles: datos.detalles.map((d: any) => ({
            cantidad: d.cantidad,
            producto: String(d.productoId),
          })),
        }).subscribe({
          next: () => this.onSaved('Nota de venta actualizada'),
          error: (err) => this.onError(err, 'Error al actualizar nota'),
        });
      } else {
        this.notaventaService.registrar(datos).subscribe({
          next: () => this.onSaved('Nota de venta creada'),
          error: (err) => this.onError(err, 'Error al crear nota'),
        });
      }
    }
  }

  private onSaved(msg: string): void {
    this.guardando = false;
    this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
    this.dialogRef?.close('saved');
  }

  private onError(err: any, fallback: string): void {
    this.guardando = false;
    this.snackBar.open(err.error?.error || fallback, 'Cerrar', { duration: 5000 });
  }

  cancelar(): void {
    this.dialogRef?.close();
  }
}
