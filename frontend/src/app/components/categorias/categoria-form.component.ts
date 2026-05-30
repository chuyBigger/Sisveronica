import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CategoriaService } from '../../services/categoria.service';
import { EnumsService } from '../../services/enums.service';

@Component({
  selector: 'app-categoria-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './categoria-form.component.html',
  styleUrl: './categoria-form.component.scss',
})
export class CategoriaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private categoriaService = inject(CategoriaService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);

  esEdicion = false;
  categoriaId: number | null = null;
  cargando = false;
  partidas: string[] = [];

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    partida: ['', Validators.required],
  });

  ngOnInit(): void {
    this.enumsService.getPartidas().subscribe((res) => (this.partidas = res));
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.esEdicion = true;
      this.categoriaId = Number(idParam);
      this.cargarCategoria(this.categoriaId);
    }
  }

  cargarCategoria(id: number): void {
    this.cargando = true;
    this.categoriaService.buscarPorId(id).subscribe({
      next: (cat) => {
        this.form.patchValue(cat);
        this.cargando = false;
      },
      error: () => {
        this.snackBar.open('Error al cargar categoría', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) return;
    const datos = this.form.value;

    if (this.esEdicion && this.categoriaId) {
      this.categoriaService.actualizar(this.categoriaId, datos).subscribe({
        next: () => {
          this.snackBar.open('Categoría actualizada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/categorias']);
        },
        error: () => this.snackBar.open('Error al actualizar categoría', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.categoriaService.registrar(datos).subscribe({
        next: () => {
          this.snackBar.open('Categoría creada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/categorias']);
        },
        error: () => this.snackBar.open('Error al crear categoría', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/categorias']);
  }
}
