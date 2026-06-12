import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  MAT_DIALOG_DATA, MatDialogRef, MatDialogModule, MatDialog,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NotaVentaService } from '../../services/notaventa.service';
import { DatosListarNota } from '../../models/notaventa.model';
import { DetalleDialogComponent, DetalleDialogData } from './detalle-dialog.component';

@Component({
  selector: 'app-notaventa-preview-dialog',
  standalone: true,
  imports: [
    CommonModule, MatDialogModule, MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDividerModule, MatTooltipModule,
  ],
  templateUrl: './notaventa-preview-dialog.component.html',
  styleUrl: './notaventa-preview-dialog.component.scss',
})
export class NotaVentaPreviewDialogComponent {
  private router = inject(Router);
  private notaventaService = inject(NotaVentaService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<NotaVentaPreviewDialogComponent>);
  private dialog = inject(MatDialog);

  constructor(@Inject(MAT_DIALOG_DATA) public nota: DatosListarNota) {}

  readonly negocio = {
    nombre: 'CARNICERÍA "LA VERÓNICA"',
    eslogan: 'La Mejor Calidad',
    fiscal: 'Jesus Manuel Romo Alba',
    rfc: 'R.F.C. ROAJ600629RQ5',
    domicilio: 'C. ALEGRIA #211 BARRIO DEL ENCINO C. P. 20240, Aguascalientes, Ags.',
  };

  imprimir(): void {
    const count = this.nota?.detalles?.length ?? 0;
    const size = count > 8 ? '108mm 186mm' : '108mm 140mm';
    const fontSize = count > 10 ? 8 : count > 6 ? 9 : 10;

    const style = document.createElement('style');
    style.id = 'print-size';
    style.textContent = `@page { size: ${size}; margin: 0.6cm; }
#print-area, #print-area * { font-size: ${fontSize}px !important; }`;
    document.head.appendChild(style);

    document.body.classList.add('printing-dialog');
    window.print();

    const cleanup = () => {
      document.body.classList.remove('printing-dialog');
      document.getElementById('print-size')?.remove();
      window.removeEventListener('afterprint', cleanup);
    };
    window.addEventListener('afterprint', cleanup);
  }

  editar(): void {
    this.dialogRef.close({ action: 'edit', notaId: this.nota.id });
  }

  borrar(): void {
    if (confirm(`¿Eliminar nota Folio #${this.nota.folio}?`)) {
      this.notaventaService.eliminar(this.nota.id).subscribe({
        next: () => {
          this.snackBar.open(`Nota Folio #${this.nota.folio} eliminada`, 'Cerrar', { duration: 3000 });
          this.dialogRef.close('deleted');
        },
        error: () => this.snackBar.open('Error al eliminar nota', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cerrar(): void {
    this.dialogRef.close();
  }

  firmarNota(): void {
    this.notaventaService.firmar(this.nota.id).subscribe({
      next: () => {
        this.nota.firmada = true;
        this.snackBar.open('Nota firmada', 'Cerrar', { duration: 2000 });
      },
      error: () => this.snackBar.open('Error al firmar nota', 'Cerrar', { duration: 3000 }),
    });
  }

  editarDetalle(): void {
    const dialogRef = this.dialog.open(DetalleDialogComponent, {
      width: '500px',
      data: { detalleActual: this.nota.detalle, folio: this.nota.folio } as DetalleDialogData,
    });
    dialogRef.afterClosed().subscribe((detalle) => {
      if (detalle !== undefined) {
        this.notaventaService.actualizarDetalle(this.nota.id, detalle).subscribe({
          next: () => {
            this.nota.detalle = detalle;
            this.snackBar.open('Detalle actualizado', 'Cerrar', { duration: 2000 });
          },
          error: () => this.snackBar.open('Error al actualizar detalle', 'Cerrar', { duration: 3000 }),
        });
      }
    });
  }
}
