import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { CategoriaService } from '../../services/categoria.service';
import { DatosDetalleCategoria } from '../../models/categoria.model';

@Component({
  selector: 'app-categoria-lista',
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
  templateUrl: './categoria-lista.component.html',
  styleUrl: './categoria-lista.component.scss',
})
export class CategoriaListaComponent implements OnInit {
  private categoriaService = inject(CategoriaService);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'nombre', 'partida', 'acciones'];
  categorias: DatosDetalleCategoria[] = [];

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.categoriaService.listar().subscribe({
      next: (res) => (this.categorias = res),
      error: () => this.snackBar.open('Error al cargar categorías', 'Cerrar', { duration: 3000 }),
    });
  }

  confirmarEliminar(id: number, nombre: string): void {
    if (confirm(`¿Eliminar la categoría "${nombre}"?`)) {
      this.categoriaService.eliminar(id).subscribe({
        next: () => {
          this.snackBar.open('Categoría eliminada', 'Cerrar', { duration: 3000 });
          this.cargarCategorias();
        },
        error: () => this.snackBar.open('Error al eliminar categoría', 'Cerrar', { duration: 3000 }),
      });
    }
  }
}
