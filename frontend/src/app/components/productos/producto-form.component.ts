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
import { ProductoService } from '../../services/producto.service';
import { CategoriaService } from '../../services/categoria.service';
import { EnumsService } from '../../services/enums.service';
import { DatosDetalleCategoria } from '../../models/categoria.model';

@Component({
  selector: 'app-producto-form',
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
  templateUrl: './producto-form.component.html',
  styleUrl: './producto-form.component.scss',
})
export class ProductoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private productoService = inject(ProductoService);
  private categoriaService = inject(CategoriaService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);

  esEdicion = false;
  productoId: string | null = null;
  cargando = false;
  categorias: DatosDetalleCategoria[] = [];
  partidas: string[] = [];
  unidadesMedida: string[] = [];

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    partida: ['', Validators.required],
    categoriaId: ['', Validators.required],
    unidadMedida: ['', Validators.required],
    codigo: [''],
    precioCompra: [null],
    precioVenta: ['', Validators.required],
  });

  ngOnInit(): void {
    this.cargarCatalogos();
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.esEdicion = true;
      this.productoId = idParam;
      this.cargarProducto(this.productoId);
    }
  }

  cargarCatalogos(): void {
    this.enumsService.getPartidas().subscribe({
      next: (res) => (this.partidas = res),
      error: () => this.snackBar.open('Error al cargar partidas', 'Cerrar', { duration: 2000 }),
    });
    this.enumsService.getUnidadesMedida().subscribe({
      next: (res) => (this.unidadesMedida = res),
      error: () => this.snackBar.open('Error al cargar unidades', 'Cerrar', { duration: 2000 }),
    });
    this.categoriaService.listar().subscribe({
      next: (res) => (this.categorias = res),
      error: () => this.snackBar.open('Error al cargar categorías', 'Cerrar', { duration: 2000 }),
    });
  }

  cargarProducto(id: string): void {
    this.cargando = true;
    this.productoService.buscarPorId(id).subscribe({
      next: (producto) => {
        this.form.patchValue(producto);
        this.cargando = false;
      },
      error: () => {
        this.snackBar.open('Error al cargar producto', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) return;

    const datos = this.form.value;

    if (this.esEdicion && this.productoId) {
      this.productoService.actualizar(this.productoId, datos).subscribe({
        next: () => {
          this.snackBar.open('Producto actualizado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/productos']);
        },
        error: () => this.snackBar.open('Error al actualizar producto', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.productoService.registrar(datos).subscribe({
        next: () => {
          this.snackBar.open('Producto creado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/productos']);
        },
        error: () => this.snackBar.open('Error al crear producto', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/productos']);
  }
}
