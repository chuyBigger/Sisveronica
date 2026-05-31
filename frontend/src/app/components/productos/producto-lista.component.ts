import { Component, inject, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../../services/producto.service';
import { EnumsService } from '../../services/enums.service';
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
    MatSelectModule,
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
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);
  private cdr = inject(ChangeDetectorRef);

  displayedColumns: string[] = ['id', 'nombre', 'codigo', 'partida', 'categoria', 'precioVenta', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarProductos>([]);
  totalElements = 0;
  searchQuery = '';
  partidas: string[] = [];
  partidaSeleccionada = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.enumsService.getPartidas().subscribe((res) => {
      this.partidas = res;
      this.cdr.detectChanges();
    });
    this.cargarProductos();
  }

  cargarProductos(): void {
    const page = this.paginator?.pageIndex ?? 0;
    const size = this.paginator?.pageSize ?? 10;

    if (this.partidaSeleccionada) {
      this.productoService.listarPorPartida(this.partidaSeleccionada, page, size).subscribe({
        next: (res) => {
          this.dataSource.data = res.content ?? res;
          this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
          this.cdr.detectChanges();
        },
        error: () => this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 }),
      });
    } else if (this.searchQuery.trim()) {
      this.productoService.buscarPorPalabra(this.searchQuery.trim(), page, size).subscribe({
        next: (res) => {
          this.dataSource.data = res.content ?? res;
          this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
          this.cdr.detectChanges();
        },
        error: () => this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.productoService.listar(page, size).subscribe({
        next: (res) => {
          this.dataSource.data = res.content ?? res;
          this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
          this.cdr.detectChanges();
        },
        error: () => this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  onPageChange(): void {
    this.cargarProductos();
  }

  filtrarPorPartida(): void {
    this.paginator.firstPage();
    this.cargarProductos();
  }

  buscar(): void {
    this.partidaSeleccionada = '';
    this.paginator.firstPage();
    this.cargarProductos();
  }

  limpiarBusqueda(): void {
    this.searchQuery = '';
    this.partidaSeleccionada = '';
    this.paginator.firstPage();
    this.cargarProductos();
  }

  confirmarEliminar(id: string, nombre: string): void {
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
