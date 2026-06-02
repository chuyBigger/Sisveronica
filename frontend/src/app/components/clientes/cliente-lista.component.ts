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
import { ClienteService } from '../../services/cliente.service';
import { DatosDetalleCliente } from '../../models/cliente.model';

@Component({
  selector: 'app-cliente-lista',
  standalone: true,
  imports: [
    CommonModule, RouterModule, FormsModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule, MatFormFieldModule, MatInputModule,
    MatCardModule, MatTooltipModule, MatSnackBarModule,
  ],
  templateUrl: './cliente-lista.component.html',
  styleUrl: './cliente-lista.component.scss',
})
export class ClienteListaComponent implements AfterViewInit {
  private clienteService = inject(ClienteService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  displayedColumns: string[] = ['rfc', 'nombre', 'municipio', 'estado', 'acciones'];
  dataSource = new MatTableDataSource<DatosDetalleCliente>([]);
  totalElements = 0;
  searchQuery = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = (data, filter) => {
      const q = filter.toLowerCase();
      return data.nombre.toLowerCase().includes(q) ||
             data.rfc.toLowerCase().includes(q) ||
             (data.municipio || '').toLowerCase().includes(q);
    };
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clienteService.listar().subscribe({
      next: (res) => {
        this.dataSource.data = res;
        this.totalElements = res.length;
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar clientes', 'Cerrar', { duration: 3000 }),
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

  verCliente(id: string): void {
    this.router.navigate(['/clientes', id]);
  }

  confirmarEliminar(id: string, nombre: string): void {
    if (confirm(`¿Eliminar el cliente "${nombre}"?`)) {
      this.clienteService.eliminar(id).subscribe({
        next: () => {
          this.snackBar.open('Cliente eliminado', 'Cerrar', { duration: 3000 });
          this.cargarClientes();
        },
        error: () => this.snackBar.open('Error al eliminar cliente', 'Cerrar', { duration: 3000 }),
      });
    }
  }
}
