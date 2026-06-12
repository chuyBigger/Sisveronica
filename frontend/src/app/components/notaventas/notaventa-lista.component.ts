import { Component, inject, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
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
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { NotaVentaService } from '../../services/notaventa.service';
import { EnumsService } from '../../services/enums.service';
import { DatosListarNota } from '../../models/notaventa.model';
import { NotaVentaPreviewDialogComponent } from './notaventa-preview-dialog.component';
import { NotaVentaFormDialogComponent, NotaVentaFormData } from './notaventa-form-dialog.component';
import { DetalleDialogComponent, DetalleDialogData } from './detalle-dialog.component';

@Component({
  selector: 'app-notaventa-lista',
  standalone: true,
  imports: [
    CommonModule, RouterModule, FormsModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule, MatSelectModule,
    MatCardModule, MatTooltipModule, MatSnackBarModule, MatDialogModule,
  ],
  templateUrl: './notaventa-lista.component.html',
  styleUrl: './notaventa-lista.component.scss',
})
export class NotaVentaListaComponent implements AfterViewInit {
  private notaventaService = inject(NotaVentaService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);
  private dialog = inject(MatDialog);

  displayedColumns: string[] = ['folio', 'fecha', 'cliente', 'partida', 'totalGeneral', 'estado', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarNota>([]);
  totalElements = 0;
  searchQuery = '';
  partidas: string[] = [];
  partidaSeleccionada = '';
  filtroDetalle = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = (data, filter) => {
      const parts = filter.split('|');
      const q = parts[0]?.toLowerCase() || '';
      const detalleFilter = parts[1] || '';
      const matchSearch = !q ||
        (data.folio?.toString().includes(q) ?? false) ||
        data.cliente.toLowerCase().includes(q) ||
        data.partida.toLowerCase().includes(q);
      if (!matchSearch) return false;
      if (detalleFilter === 'con') return !!data.detalle;
      if (detalleFilter === 'sin') return !data.detalle;
      return true;
    };
    this.enumsService.getPartidas().subscribe((res) => {
      this.partidas = res;
      this.cdr.detectChanges();
    });
    this.cargarNotas();
  }

  cargarNotas(): void {
    const page = this.paginator?.pageIndex ?? 0;
    const size = this.paginator?.pageSize ?? 10;

    this.notaventaService.listar(page, size).subscribe({
      next: (res) => {
        this.dataSource.data = res.content ?? res;
        this.totalElements = res.totalElements ?? (res.content ? res.content.length : res.length);
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar notas de venta', 'Cerrar', { duration: 3000 }),
    });
  }

  onPageChange(): void {
    this.cargarNotas();
  }

  filtrar(): void {
    const parts: string[] = [];
    parts.push(this.searchQuery.trim().toLowerCase());
    parts.push(this.filtroDetalle);
    this.dataSource.filter = parts.join('|');
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  limpiarBusqueda(): void {
    this.searchQuery = '';
    this.partidaSeleccionada = '';
    this.filtroDetalle = '';
    this.dataSource.filter = '';
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  abrirCrear(): void {
    const dialogRef = this.dialog.open(NotaVentaFormDialogComponent, {
      data: { mode: 'create' } as NotaVentaFormData,
      width: '900px',
      maxWidth: '95vw',
      panelClass: 'notaventa-preview-dialog',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'saved') this.cargarNotas();
    });
  }

  abrirEditar(notaId: string): void {
    const dialogRef = this.dialog.open(NotaVentaFormDialogComponent, {
      data: { mode: 'edit', notaId } as NotaVentaFormData,
      width: '900px',
      maxWidth: '95vw',
      panelClass: 'notaventa-preview-dialog',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'saved') this.cargarNotas();
    });
  }

  abrirPreview(row: DatosListarNota): void {
    const dialogRef = this.dialog.open(NotaVentaPreviewDialogComponent, {
      data: row,
      width: '900px',
      maxWidth: '95vw',
      panelClass: 'notaventa-preview-dialog',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'deleted') {
        this.cargarNotas();
      } else if (result?.action === 'edit') {
        this.abrirEditar(result.notaId);
      }
    });
  }

  confirmarEliminar(id: string): void {
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

  firmarNota(id: string, event: Event): void {
    event.stopPropagation();
    this.notaventaService.firmar(id).subscribe({
      next: () => {
        this.snackBar.open('Nota firmada', 'Cerrar', { duration: 2000 });
        this.cargarNotas();
      },
      error: () => this.snackBar.open('Error al firmar nota', 'Cerrar', { duration: 3000 }),
    });
  }

  editarDetalle(row: DatosListarNota, event: Event): void {
    event.stopPropagation();
    const dialogRef = this.dialog.open(DetalleDialogComponent, {
      width: '500px',
      data: { detalleActual: row.detalle, folio: row.folio } as DetalleDialogData,
    });
    dialogRef.afterClosed().subscribe((detalle) => {
      if (detalle !== undefined) {
        this.notaventaService.actualizarDetalle(row.id, detalle).subscribe({
          next: () => {
            this.snackBar.open('Detalle actualizado', 'Cerrar', { duration: 2000 });
            this.cargarNotas();
          },
          error: () => this.snackBar.open('Error al actualizar detalle', 'Cerrar', { duration: 3000 }),
        });
      }
    });
  }

  getRowClass(row: DatosListarNota): string {
    if (row.firmada) return 'row-firmada';
    if (row.detalle) return 'row-alerta';
    return '';
  }
}
