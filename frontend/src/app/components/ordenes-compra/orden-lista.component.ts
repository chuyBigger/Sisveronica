import { Component, inject, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, formatDate } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { OrdenCompraService } from '../../services/ordencompra.service';
import { DatosListarOrdenCompra } from '../../models/ordencompra.model';

@Component({
  selector: 'app-orden-lista',
  standalone: true,
  imports: [
    CommonModule, RouterModule, FormsModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule,
    MatDatepickerModule, MatNativeDateModule,
    MatCardModule, MatTooltipModule, MatSnackBarModule,
  ],
  templateUrl: './orden-lista.component.html',
  styleUrl: './orden-lista.component.scss',
})
export class OrdenListaComponent implements AfterViewInit {
  private ordenService = inject(OrdenCompraService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  displayedColumns: string[] = ['contrato', 'cliente', 'partida', 'fechaInicioSemana', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarOrdenCompra>([]);
  totalElements = 0;
  searchQuery = '';
  fechaSeleccionada: Date | null = null;

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
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    const page = this.paginator?.pageIndex ?? 0;
    const size = this.paginator?.pageSize ?? 10;
    const fechaStr = this.fechaSeleccionada
      ? formatDate(this.fechaSeleccionada, 'yyyy-MM-dd', 'en-US')
      : undefined;

    this.ordenService.listar(page, size, fechaStr).subscribe({
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

  onFechaChange(): void {
    if (this.paginator) {
      this.paginator.firstPage();
    }
    this.cargarOrdenes();
  }

  filtrar(): void {
    const parts: string[] = [];
    if (this.searchQuery.trim()) {
      parts.push(this.searchQuery.trim().toLowerCase());
    }
    this.dataSource.filter = parts.join(' ');
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  limpiarBusqueda(): void {
    this.searchQuery = '';
    this.fechaSeleccionada = null;
    this.dataSource.filter = '';
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    this.cargarOrdenes();
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
