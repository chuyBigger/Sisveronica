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

  displayedColumns: string[] = ['folio', 'fecha', 'cliente', 'partida', 'totalGeneral', 'acciones'];
  dataSource = new MatTableDataSource<DatosListarNota>([]);
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
      const folioMatch = data.folio?.toString().includes(q) ?? false;
      return folioMatch ||
             data.cliente.toLowerCase().includes(q) ||
             data.partida.toLowerCase().includes(q);
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
}
