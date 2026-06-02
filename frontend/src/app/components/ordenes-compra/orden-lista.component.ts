import { Component, inject, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { OrdenCompraService } from '../../services/ordencompra.service';
import { EnumsService } from '../../services/enums.service';
import { DatosListarOrdenCompra } from '../../models/ordencompra.model';

@Component({
  selector: 'app-orden-lista',
  standalone: true,
  imports: [
    CommonModule, RouterModule, FormsModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule, MatSelectModule,
    MatCardModule, MatTooltipModule, MatSnackBarModule,
  ],
  templateUrl: './orden-lista.component.html',
  styleUrl: './orden-lista.component.scss',
})
export class OrdenListaComponent implements AfterViewInit {
  private ordenService = inject(OrdenCompraService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  displayedColumns: string[] = ['contrato', 'cliente', 'partida', 'fechaInicioSemana', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarOrdenCompra>([]);
  totalElements = 0;
  searchQuery = '';
  partidas: string[] = [];
  partidaSeleccionada = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = (data, filter) => {
      const q = filter.toLowerCase();
      return data.contrato.toLowerCase().includes(q) ||
             data.cliente.toLowerCase().includes(q) ||
             data.partida.toLowerCase().includes(q);
    };
    this.enumsService.getPartidas().subscribe((res) => {
      this.partidas = res;
      this.cdr.detectChanges();
    });
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    const page = this.paginator?.pageIndex ?? 0;
    const size = this.paginator?.pageSize ?? 10;

    this.ordenService.listar(page, size).subscribe({
      next: (res) => {
        this.dataSource.data = res.content ?? res;
        this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar órdenes de compra', 'Cerrar', { duration: 3000 }),
    });
  }

  onPageChange(): void {
    this.cargarOrdenes();
  }

  filtrar(): void {
    const parts: string[] = [];
    if (this.searchQuery.trim()) {
      parts.push(this.searchQuery.trim().toLowerCase());
    }
    if (this.partidaSeleccionada) {
      parts.push(this.partidaSeleccionada.toLowerCase());
    }
    this.dataSource.filter = parts.join(' ');
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  limpiarBusqueda(): void {
    this.searchQuery = '';
    this.partidaSeleccionada = '';
    this.dataSource.filter = '';
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  verOrden(id: string): void {
    this.router.navigate(['/ordenes-compra', id, 'ver']);
  }

  confirmarEliminar(id: string): void {
    if (confirm('¿Eliminar esta orden de compra?')) {
      this.ordenService.eliminar(id).subscribe({
        next: () => {
          this.snackBar.open('Orden de compra eliminada', 'Cerrar', { duration: 3000 });
          this.cargarOrdenes();
        },
        error: () => this.snackBar.open('Error al eliminar orden de compra', 'Cerrar', { duration: 3000 }),
      });
    }
  }
}
