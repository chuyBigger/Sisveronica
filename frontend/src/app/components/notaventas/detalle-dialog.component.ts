import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

export interface DetalleDialogData {
  detalleActual?: string;
  folio?: number;
}

@Component({
  selector: 'app-detalle-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatDialogModule,
    MatButtonModule, MatFormFieldModule, MatInputModule,
  ],
  template: `
    <h2 mat-dialog-title>Detalle de incidencia</h2>
    <mat-dialog-content>
      <p>Nota #{{ data.folio }}</p>
      <mat-form-field appearance="outline" class="full-width">
        <mat-label>Detalle</mat-label>
        <textarea matInput [(ngModel)]="detalle" rows="5" placeholder="Ej: regresaron la carne porque olía feo 25kg res solo10"></textarea>
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancelar</button>
      <button mat-raised-button color="primary" [mat-dialog-close]="detalle">Guardar</button>
    </mat-dialog-actions>
  `,
  styles: ['.full-width { width: 100%; }'],
})
export class DetalleDialogComponent {
  private dialogRef = inject(MatDialogRef<DetalleDialogComponent>);
  detalle: string;

  constructor(@Inject(MAT_DIALOG_DATA) public data: DetalleDialogData) {
    this.detalle = data.detalleActual ?? '';
  }
}
