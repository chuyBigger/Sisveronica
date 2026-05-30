import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ClienteService } from '../../services/cliente.service';
import { DatosDetalleCliente } from '../../models/cliente.model';

@Component({
  selector: 'app-cliente-lista',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTooltipModule,
    MatSnackBarModule,
  ],
  templateUrl: './cliente-lista.component.html',
  styleUrl: './cliente-lista.component.scss',
})
export class ClienteListaComponent implements OnInit {
  private clienteService = inject(ClienteService);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'nombre', 'rfc', 'municipio', 'estado', 'acciones'];
  clientes: DatosDetalleCliente[] = [];

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clienteService.listar().subscribe({
      next: (res) => (this.clientes = res),
      error: () => this.snackBar.open('Error al cargar clientes', 'Cerrar', { duration: 3000 }),
    });
  }

  confirmarEliminar(id: number, nombre: string): void {
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
