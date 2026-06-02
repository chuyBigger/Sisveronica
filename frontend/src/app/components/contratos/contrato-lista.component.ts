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
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { ContratoService } from '../../services/contrato.service';
import { DatosDetalleContrato } from '../../models/contrato.model';

@Component({
  selector: 'app-contrato-lista',
  standalone: true,
  imports: [
    CommonModule, RouterModule, FormsModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule,
    MatCardModule, MatTooltipModule, MatSnackBarModule,
  ],
  templateUrl: './contrato-lista.component.html',
  styleUrl: './contrato-lista.component.scss',
})
export class ContratoListaComponent implements AfterViewInit {
  private contratoService = inject(ContratoService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  displayedColumns: string[] = ['contrato', 'cliente', 'fechaInicio', 'fechaTermino', 'presupuesto', 'acciones'];
  dataSource = new MatTableDataSource<DatosDetalleContrato>([]);
  totalElements = 0;
  searchQuery = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = (data, filter) => {
      const q = filter.toLowerCase();
      return data.contrato.toLowerCase().includes(q) ||
             data.cliente.toLowerCase().includes(q);
    };
    this.cargarContratos();
  }

  cargarContratos(): void {
    this.contratoService.listar().subscribe({
      next: (res) => {
        this.dataSource.data = res;
        this.totalElements = res.length;
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar contratos', 'Cerrar', { duration: 3000 }),
    });
  }

  buscar(): void {
    this.dataSource.filter = this.searchQuery.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  limpiarBusqueda(): void {
    this.searchQuery = '';
    this.buscar();
  }

  verContrato(id: string): void {
    this.router.navigate(['/contratos', id]);
  }

  confirmarEliminar(id: string, nombre: string): void {
    if (confirm(`¿Eliminar el contrato "${nombre}"?`)) {
      this.contratoService.eliminar(id).subscribe({
        next: () => {
          this.snackBar.open('Contrato eliminado', 'Cerrar', { duration: 3000 });
          this.cargarContratos();
        },
        error: () => this.snackBar.open('Error al eliminar contrato', 'Cerrar', { duration: 3000 }),
      });
    }
  }
}
