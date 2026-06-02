import { Component, inject, Inject, ChangeDetectorRef } from '@angular/core';
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
import { CancelacionService } from '../../services/cancelacion.service';
import { DatosDetalleOrdenCompra, DatosDetalleOrdenCompraDetalle } from '../../models/ordencompra.model';

export interface CancelacionFormData {
  orden: DatosDetalleOrdenCompra;
}

@Component({
  selector: 'app-cancelacion-form-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatDialogModule, MatButtonModule, MatIconModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatCardModule, MatSnackBarModule,
  ],
  template: `
    <div class="dialog-content">
      <h2 mat-dialog-title>Nueva Cancelación</h2>
      <mat-dialog-content>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Día</mat-label>
          <mat-select [(ngModel)]="diaSeleccionado" (selectionChange)="onDiaChange()">
            @for (d of dias; track d; let i = $index) {
              <mat-option [value]="d">{{ diasCorto[i] }} ({{ d }})</mat-option>
            }
          </mat-select>
        </mat-form-field>

        @if (diaSeleccionado && productos.length > 0) {
          <div class="productos-list">
            @for (p of productos; track p.producto; let i = $index) {
              <div class="producto-row">
                <span class="prod-nombre">{{ p.productoNombre }}</span>
                <span class="prod-oc">OC: {{ getCantidadOC(p) }}</span>
                <mat-form-field appearance="outline" subscriptSizing="dynamic" class="cant-input">
                  <mat-label>Cancelar</mat-label>
                  <input matInput type="number" min="0" [max]="getCantidadOC(p)" step="0.5"
                         [(ngModel)]="cantidades[i]" (input)="validarCantidad(i)">
                </mat-form-field>
              </div>
            }
          </div>
        }

        @if (diaSeleccionado && productos.length === 0) {
          <p class="sin-prod">No hay productos con cantidades para este día.</p>
        }
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button (click)="cerrar()">Cancelar</button>
        <button mat-raised-button color="primary" (click)="guardar()"
                [disabled]="!diaSeleccionado || !tieneCancelaciones()">
          <mat-icon>check</mat-icon> Crear Cancelación
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-content { min-width: 500px; padding: 8px 0; }
    .full-width { width: 100%; margin-bottom: 16px; }
    .productos-list { display: flex; flex-direction: column; gap: 8px; max-height: 400px; overflow-y: auto; }
    .producto-row { display: flex; align-items: center; gap: 12px; padding: 8px; border: 1px solid #eee; border-radius: 4px; }
    .prod-nombre { flex: 1; font-size: 0.9rem; }
    .prod-oc { font-size: 0.8rem; color: #888; min-width: 60px; }
    .cant-input { width: 120px; }
    .sin-prod { color: #888; font-style: italic; text-align: center; padding: 20px; }
  `],
})
export class CancelacionFormDialogComponent {
  private cancelacionService = inject(CancelacionService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);
  private dialogRef = inject(MatDialogRef<CancelacionFormDialogComponent>);

  constructor(@Inject(MAT_DIALOG_DATA) public data: CancelacionFormData) {}

  readonly dias = ['lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'];
  readonly diasCorto = ['Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá', 'Do'];

  diaSeleccionado = '';
  productos: DatosDetalleOrdenCompraDetalle[] = [];
  cantidades: number[] = [];

  onDiaChange(): void {
    this.productos = this.data.orden.detalles.filter(d => (d as any)[this.diaSeleccionado] > 0);
    this.cantidades = this.productos.map(() => 0);
    this.cdr.detectChanges();
  }

  getCantidadOC(detalle: any): number {
    return detalle[this.diaSeleccionado] || 0;
  }

  validarCantidad(i: number): void {
    const max = this.getCantidadOC(this.productos[i]);
    if (this.cantidades[i] > max) this.cantidades[i] = max;
    if (this.cantidades[i] < 0) this.cantidades[i] = 0;
  }

  tieneCancelaciones(): boolean {
    return this.cantidades.some(c => c > 0);
  }

  guardar(): void {
    const detalles = this.productos
      .map((p, i) => ({ productoId: p.producto, cantidadCancelada: this.cantidades[i] }))
      .filter(d => d.cantidadCancelada > 0);

    if (detalles.length === 0) return;

    this.cancelacionService.crear({
      ordenCompraId: this.data.orden.id,
      dia: this.diaSeleccionado,
      detalles,
    }).subscribe({
      next: () => {
        this.snackBar.open('Cancelación creada', 'Cerrar', { duration: 2000 });
        this.dialogRef.close('saved');
      },
      error: () => this.snackBar.open('Error al crear cancelación', 'Cerrar', { duration: 3000 }),
    });
  }

  cerrar(): void {
    this.dialogRef.close();
  }
}
