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
    const printContent = document.getElementById('print-area');
    if (!printContent) return;
    const win = window.open('', '_blank');
    if (!win) return;
    win.document.write(`
      <html>
        <head>
          <style>
            @page { size: 21.59cm 13.97cm; margin: 0.5cm; }
            * { box-sizing: border-box; margin: 0; padding: 0; }
            body {
              font-family: 'Courier New', Courier, monospace;
              font-size: 11px;
              color: #000;
              padding: 0.3cm;
            }
            .nota-remision {
              max-width: 100%;
            }
            .header { border-bottom: 2px solid #000; padding-bottom: 0.5rem; margin-bottom: 0.6rem; }
            .header-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.4rem; }
            .header-left { display: flex; gap: 0.5rem; align-items: center; }
            .logo-img { width: 50px; height: auto; }
            .brand { }
            .business-name { font-size: 1.1rem; font-weight: 700; text-transform: uppercase; }
            .slogan { font-size: 0.7rem; color: #555; font-style: italic; }
            .folio-box { text-align: center; border: 2px solid #000; padding: 0.3rem 0.8rem; min-width: 100px; }
            .folio-label { display: block; font-size: 0.6rem; font-weight: 600; text-transform: uppercase; }
            .folio-number { display: block; font-size: 1.3rem; font-weight: 700; }
            .fiscal-info { font-size: 0.7rem; line-height: 1.4; }
            .fiscal-info p { margin: 0; }
            .control-section { border: 1px solid #000; padding: 0.4rem 0.6rem; margin-bottom: 0.6rem; }
            .control-row { display: flex; gap: 0.3rem; margin-bottom: 0.15rem; font-size: 0.75rem; }
            .control-label { font-weight: 600; min-width: 70px; }
            .detalle-table { width: 100%; border-collapse: collapse; font-size: 0.75rem; margin-bottom: 0.6rem; }
            .detalle-table th, .detalle-table td { border: 1px solid #000; padding: 4px 6px; text-align: left; }
            .detalle-table th { background: #e8e8e8; font-weight: 700; text-transform: uppercase; font-size: 0.65rem; }
            .detalle-table .col-no { width: 30px; text-align: center; }
            .detalle-table .col-cantidad { text-align: center; }
            .detalle-table .col-precio, .detalle-table .col-total { text-align: right; }
            .detalle-table tfoot .total-row { background: #e0e0e0; font-weight: 700; }
            .detalle-table tfoot .total-label { text-align: right; padding-right: 0.8rem; text-transform: uppercase; }
            .detalle-table tfoot .total-amount { border: 2px solid #000; font-size: 0.85rem; }
            .signature-section { border-top: 1px solid #000; padding-top: 0.8rem; margin-top: 0.8rem; }
            .signature-label { font-weight: 600; font-size: 0.75rem; text-transform: uppercase; }
            .signature-space { height: 40px; border-bottom: 1px solid #000; width: 50%; }
            .signature-sub { font-size: 0.7rem; color: #555; margin-top: 0.15rem; }
            @media print {
              body { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
            }
          </style>
        </head>
        <body>
          ${printContent.innerHTML}
        </body>
      </html>
    `);
    win.document.close();
    setTimeout(() => { win.print(); win.close(); }, 500);
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
