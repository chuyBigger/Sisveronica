import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  MAT_DIALOG_DATA, MatDialogRef, MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { NotaVentaService } from '../../services/notaventa.service';
import { DatosListarNota } from '../../models/notaventa.model';

@Component({
  selector: 'app-notaventa-preview-dialog',
  standalone: true,
  imports: [
    CommonModule, MatDialogModule, MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDividerModule,
  ],
  templateUrl: './notaventa-preview-dialog.component.html',
  styleUrl: './notaventa-preview-dialog.component.scss',
})
export class NotaVentaPreviewDialogComponent {
  private router = inject(Router);
  private notaventaService = inject(NotaVentaService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<NotaVentaPreviewDialogComponent>);

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
}
