import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ProductoExcelService, ReporteCargaProductos } from '../../services/producto-excel.service';
import { EnumsService } from '../../services/enums.service';

@Component({
  selector: 'app-producto-excel-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatDialogModule, MatButtonModule, MatIconModule,
    MatSnackBarModule, MatProgressSpinnerModule, MatDividerModule,
    MatSelectModule, MatFormFieldModule,
  ],
  templateUrl: './producto-excel-dialog.component.html',
  styleUrl: './producto-excel-dialog.component.scss',
})
export class ProductoExcelDialogComponent implements OnInit {
  private productoExcelService = inject(ProductoExcelService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<ProductoExcelDialogComponent>);

  partidas: string[] = [];
  partidaExportar = '';

  cargando = false;
  archivoSeleccionado: File | null = null;
  reporte: ReporteCargaProductos | null = null;

  ngOnInit(): void {
    this.enumsService.getPartidas().subscribe(res => this.partidas = res);
  }

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

  descargarProductos(): void {
    if (!this.partidaExportar) return;
    this.productoExcelService.exportarProductos(this.partidaExportar).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `productos_${this.partidaExportar}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.snackBar.open(`Productos de ${this.partidaExportar} descargados`, 'Cerrar', { duration: 3000 });
      },
      error: () => this.snackBar.open('Error al descargar productos', 'Cerrar', { duration: 3000 }),
    });
  }

  cerrar(): void {
    this.dialogRef.close(this.reporte ? 'uploaded' : null);
  }
}
