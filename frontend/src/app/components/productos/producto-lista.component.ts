import { Component, inject, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../../services/producto.service';
import { DatosListarProductos } from '../../models/producto.model';

@Component({
  selector: 'app-producto-lista',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatDialogModule,
  ],
  templateUrl: './producto-lista.component.html',
  styleUrl: './producto-lista.component.scss',
})
export class ProductoListaComponent implements AfterViewInit {
  private productoService = inject(ProductoService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  displayedColumns: string[] = ['id', 'nombre', 'partida', 'categoria', 'precioVenta', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarProductos>([]);
  totalElements = 0;
  searchQuery = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.cargarProductos();
  }

  cargarProductos(): void {
    const page = this.paginator?.pageIndex ?? 0;
    const size = this.paginator?.pageSize ?? 10;

    if (this.searchQuery.trim()) {
      this.productoService.buscarPorPalabra(this.searchQuery.trim(), page, size).subscribe({
        next: (res) => {
          this.dataSource.data = res.content ?? res;
          this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
        },
        error: () => this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.productoService.listar(page, size).subscribe({
        next: (res) => {
          this.dataSource.data = res.content ?? res;
          this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
        },
        error: () => this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  onPageChange(): void {
    this.cargarProductos();
  }

  buscar(): void {
    this.paginator.firstPage();
    this.cargarProductos();
  }

  limpiarBusqueda(): void {
    this.searchQuery = '';
    this.paginator.firstPage();
    this.cargarProductos();
  }

  confirmarEliminar(id: number, nombre: string): void {
    if (confirm(`¿Eliminar el producto "${nombre}"?`)) {
      this.productoService.eliminar(id).subscribe({
        next: () => {
          this.snackBar.open('Producto eliminado', 'Cerrar', { duration: 3000 });
          this.cargarProductos();
        },
        error: () => this.snackBar.open('Error al eliminar producto', 'Cerrar', { duration: 3000 }),
      });
    }
  }
}
