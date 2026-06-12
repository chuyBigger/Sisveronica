import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { ProductoExcelService, ReporteCargaProductos } from '../../services/producto-excel.service';

@Component({
  selector: 'app-producto-excel-dialog',
  standalone: true,
  imports: [
    CommonModule, MatDialogModule, MatButtonModule, MatIconModule,
    MatSnackBarModule, MatProgressSpinnerModule, MatDividerModule,
  ],
  templateUrl: './producto-excel-dialog.component.html',
  styleUrl: './producto-excel-dialog.component.scss',
})
export class ProductoExcelDialogComponent {
  private productoExcelService = inject(ProductoExcelService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<ProductoExcelDialogComponent>);

  cargando = false;
  archivoSeleccionado: File | null = null;
  reporte: ReporteCargaProductos | null = null;

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.archivoSeleccionado = input.files[0];
    }
  }

  cargar(): void {
    if (!this.archivoSeleccionado) return;

    this.cargando = true;
    this.productoExcelService.cargarProductos(this.archivoSeleccionado).subscribe({
      next: (reporte) => {
        this.reporte = reporte;
        this.cargando = false;
        this.snackBar.open(`Carga completada: ${reporte.exitosos} exitosos`, 'Cerrar', { duration: 5000 });
      },
      error: () => {
        this.snackBar.open('Error al cargar el archivo', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  descargarPlantilla(): void {
    this.productoExcelService.descargarPlantilla().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'plantilla_productos.xlsx';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => this.snackBar.open('Error al descargar plantilla', 'Cerrar', { duration: 3000 }),
    });
  }

  cerrar(): void {
    this.dialogRef.close(this.reporte ? 'uploaded' : null);
  }
}
