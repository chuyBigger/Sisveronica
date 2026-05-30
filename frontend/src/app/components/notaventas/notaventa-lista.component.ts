import { Component, inject, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { NotaVentaService } from '../../services/notaventa.service';
import { DatosListarNota, NotaVentaListarDetalle } from '../../models/notaventa.model';

@Component({
  selector: 'app-notaventa-lista',
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
    MatChipsModule,
  ],
  templateUrl: './notaventa-lista.component.html',
  styleUrl: './notaventa-lista.component.scss',
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class NotaVentaListaComponent implements AfterViewInit {
  private notaventaService = inject(NotaVentaService);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'fecha', 'cliente', 'partida', 'totalGeneral', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarNota>([]);
  totalElements = 0;
  expandedElement: DatosListarNota | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit(): void {
    this.cargarNotas();
  }

  cargarNotas(): void {
    const page = this.paginator?.pageIndex ?? 0;
    const size = this.paginator?.pageSize ?? 10;

    this.notaventaService.listar(page, size).subscribe({
      next: (res) => {
        this.dataSource.data = res.content ?? res;
        this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
      },
      error: () => this.snackBar.open('Error al cargar notas de venta', 'Cerrar', { duration: 3000 }),
    });
  }

  onPageChange(): void {
    this.cargarNotas();
  }

  toggleExpand(row: DatosListarNota): void {
    this.expandedElement = this.expandedElement === row ? null : row;
  }

  confirmarEliminar(id: number): void {
    if (confirm('¿Eliminar esta nota de venta?')) {
      this.notaventaService.eliminar(id).subscribe({
        next: () => {
          this.snackBar.open('Nota de venta eliminada', 'Cerrar', { duration: 3000 });
          this.cargarNotas();
        },
        error: () => this.snackBar.open('Error al eliminar nota de venta', 'Cerrar', { duration: 3000 }),
      });
    }
  }
}
