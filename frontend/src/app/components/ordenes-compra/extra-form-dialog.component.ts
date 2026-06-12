import { Component, inject, OnInit, Inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ExtraService } from '../../services/extra.service';
import { ProductoService } from '../../services/producto.service';
import { DatosDetalleOrdenCompra } from '../../models/ordencompra.model';

interface ProductoExtra {
  id: string;
  nombre: string;
  cantidad: number;
}

@Component({
  selector: 'app-extra-form-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatDialogModule, MatButtonModule, MatIconModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatCardModule, MatSnackBarModule,
  ],
  template: `
    <div class="dialog-content">
      <h2 mat-dialog-title>Nuevo Extra — {{ data.orden.partida }}</h2>
      <mat-dialog-content>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Día</mat-label>
          <mat-select [(ngModel)]="diaSeleccionado">
            @for (d of dias; track d; let i = $index) {
              <mat-option [value]="d">{{ diasCorto[i] }} ({{ d }})</mat-option>
            }
          </mat-select>
        </mat-form-field>

        @if (productos.length > 0) {
          <div class="productos-list">
            @for (p of productos; track p.id; let i = $index) {
              <div class="producto-row">
                <span class="prod-nombre">{{ p.nombre }}</span>
                <mat-form-field appearance="outline" subscriptSizing="dynamic" class="cant-input">
                  <mat-label>Kg</mat-label>
                  <input matInput type="number" min="0" step="0.5" [(ngModel)]="p.cantidad">
                </mat-form-field>
              </div>
            }
          </div>
        } @else {
          <p class="sin-prod">Cargando productos...</p>
        }
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button (click)="cerrar()">Cancelar</button>
        <button mat-raised-button color="primary" (click)="guardar()"
                [disabled]="!diaSeleccionado || !tieneExtras()">
          <mat-icon>add_circle</mat-icon> Crear Extra
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-content { min-width: 520px; padding: 8px 0; }
    .full-width { width: 100%; margin-bottom: 16px; }
    .productos-list { display: flex; flex-direction: column; gap: 6px; max-height: 400px; overflow-y: auto; }
    .producto-row { display: flex; align-items: center; gap: 12px; padding: 6px 8px; border: 1px solid #eee; border-radius: 4px; }
    .prod-nombre { flex: 1; font-size: 0.9rem; }
    .cant-input { width: 120px; }
    .sin-prod { color: #888; font-style: italic; text-align: center; padding: 20px; }
  `],
})
export class ExtraFormDialogComponent implements OnInit {
  private extraService = inject(ExtraService);
  private productService = inject(ProductoService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);
  private dialogRef = inject(MatDialogRef<ExtraFormDialogComponent>);

  constructor(@Inject(MAT_DIALOG_DATA) public data: { orden: DatosDetalleOrdenCompra }) {}

  readonly dias = ['lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'];
  readonly diasCorto = ['Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá', 'Do'];

  diaSeleccionado = '';
  productos: ProductoExtra[] = [];

  ngOnInit(): void {
    this.productService.listarPorPartida(this.data.orden.partida, 0, 50).subscribe({
      next: (res) => {
        this.productos = (res.content ?? []).map((p: any) => ({
          id: p.id,
          nombre: p.nombre,
          cantidad: 0,
        }));
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 }),
    });
  }

  tieneExtras(): boolean {
    return this.productos.some(p => p.cantidad > 0);
  }

  guardar(): void {
    const detalles = this.productos
      .filter(p => p.cantidad > 0)
      .map(p => ({ productoId: p.id, cantidad: p.cantidad }));

    if (detalles.length === 0) return;

    this.extraService.crear({
      ordenCompraId: this.data.orden.id,
      dia: this.diaSeleccionado,
      detalles,
    }).subscribe({
      next: () => {
        this.snackBar.open('Extra creado', 'Cerrar', { duration: 2000 });
        this.dialogRef.close('saved');
      },
      error: () => this.snackBar.open('Error al crear extra', 'Cerrar', { duration: 3000 }),
    });
  }

  cerrar(): void {
    this.dialogRef.close();
  }
}
