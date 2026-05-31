import { Component, inject, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { OrdenCompraService } from '../../services/ordencompra.service';
import { DatosListarOrdenCompra } from '../../models/ordencompra.model';

@Component({
  selector: 'app-orden-lista',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTooltipModule,
    MatSnackBarModule,
  ],
  templateUrl: './orden-lista.component.html',
  styleUrl: './orden-lista.component.scss',
})
export class OrdenListaComponent implements AfterViewInit {
  private ordenService = inject(OrdenCompraService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);

  displayedColumns: string[] = ['id', 'cliente', 'contrato', 'partida', 'fechaInicioSemana', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarOrdenCompra>([]);
  totalElements = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit(): void {
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
