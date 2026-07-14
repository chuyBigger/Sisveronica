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
import { ExtraService } from '../../services/extra.service';
import { DetalleDialogComponent, DetalleDialogData } from './detalle-dialog.component';
import { BUSINESS_INFO } from '../../shared/business-info';

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
  private extraService = inject(ExtraService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<NotaVentaPreviewDialogComponent>);
  private dialog = inject(MatDialog);

  readonly negocio = BUSINESS_INFO;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}

  get isExtra(): boolean {
    return !('cliente' in this.data);
  }

  get label(): string {
    return this.isExtra ? 'EXTRA' : 'NOTA';
  }

  get clienteOUsuario(): string {
    return this.isExtra ? (this.data.creadoPor || '') : this.data.cliente;
  }

  get conPrecios(): boolean {
    return true;
  }

  getProductoName(d: any): string {
    return d.productoNombre || d.producto;
  }

  imprimir(): void {
    const count = this.data?.detalles?.length ?? 0;
    const size = count > 8 ? '108mm 186mm' : '108mm 140mm';
    const style = document.createElement('style');
    style.id = 'print-size';
    style.textContent = `@page { size: ${size}; margin: 0.6cm; }`;
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
    this.dialogRef.close({ action: 'edit', notaId: this.data.id });
  }

  borrar(): void {
    if (!confirm(`¿Eliminar ${this.label} #${this.data.folio}?`)) return;
    const cb = {
      next: () => {
        this.snackBar.open(`${this.label} #${this.data.folio} eliminad${this.isExtra ? 'o' : 'a'}`, 'Cerrar', { duration: 3000 });
        this.dialogRef.close('deleted');
      },
      error: () => this.snackBar.open(`Error al eliminar ${this.isExtra ? 'extra' : 'nota'}`, 'Cerrar', { duration: 3000 }),
    };
    if (this.isExtra) {
      this.extraService.eliminar(this.data.id).subscribe(cb);
    } else {
      this.notaventaService.eliminar(this.data.id).subscribe(cb);
    }
  }

  cerrar(): void {
    this.dialogRef.close();
  }

  firmar(): void {
    const cb = {
      next: () => {
        this.data.firmada = true;
        this.snackBar.open(`${this.label} firmad${this.isExtra ? 'o' : 'a'}`, 'Cerrar', { duration: 2000 });
      },
      error: () => this.snackBar.open(`Error al firmar ${this.isExtra ? 'extra' : 'nota'}`, 'Cerrar', { duration: 3000 }),
    };
    if (this.isExtra) {
      this.extraService.firmar(this.data.id).subscribe(cb);
    } else {
      this.notaventaService.firmar(this.data.id).subscribe(cb);
    }
  }

  editarDetalle(): void {
    const dialogRef = this.dialog.open(DetalleDialogComponent, {
      width: '500px',
      data: { detalleActual: this.data.detalle, folio: this.data.folio } as DetalleDialogData,
    });
    dialogRef.afterClosed().subscribe((detalle) => {
      if (detalle !== undefined) {
        this.notaventaService.actualizarDetalle(this.data.id, detalle).subscribe({
          next: () => {
            this.data.detalle = detalle;
            this.snackBar.open('Detalle actualizado', 'Cerrar', { duration: 2000 });
          },
          error: () => this.snackBar.open('Error al actualizar detalle', 'Cerrar', { duration: 3000 }),
        });
      }
    });
  }
}
