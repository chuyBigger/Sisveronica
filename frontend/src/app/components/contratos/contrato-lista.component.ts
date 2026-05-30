import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ContratoService } from '../../services/contrato.service';
import { DatosDetalleContrato } from '../../models/contrato.model';

@Component({
  selector: 'app-contrato-lista',
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
  templateUrl: './contrato-lista.component.html',
  styleUrl: './contrato-lista.component.scss',
})
export class ContratoListaComponent implements OnInit {
  private contratoService = inject(ContratoService);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'contrato', 'cliente', 'fechaInicio', 'fechaTermino', 'presupuesto', 'acciones'];
  contratos: DatosDetalleContrato[] = [];

  ngOnInit(): void {
    this.cargarContratos();
  }

  cargarContratos(): void {
    this.contratoService.listar().subscribe({
      next: (res) => (this.contratos = res),
      error: () => this.snackBar.open('Error al cargar contratos', 'Cerrar', { duration: 3000 }),
    });
  }

  confirmarEliminar(id: number, nombre: string): void {
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
