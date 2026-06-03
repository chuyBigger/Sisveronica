import { Component, inject, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  MAT_DIALOG_DATA, MatDialogRef, MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProductoService } from '../../services/producto.service';
import { EnumsService } from '../../services/enums.service';
import { CategoriaService } from '../../services/categoria.service';
import { DatosListarProductos } from '../../models/producto.model';
import { DatosDetalleCategoria } from '../../models/categoria.model';

@Component({
  selector: 'app-producto-preview-dialog',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatDialogModule, MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDividerModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatProgressSpinnerModule,
  ],
  templateUrl: './producto-preview-dialog.component.html',
  styleUrl: './producto-preview-dialog.component.scss',
})
export class ProductoPreviewDialogComponent implements OnInit {
  private fb = inject(FormBuilder);
  private productoService = inject(ProductoService);
  private enumsService = inject(EnumsService);
  private categoriaService = inject(CategoriaService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<ProductoPreviewDialogComponent>);

  editMode = false;
  cargando = false;
  partidas: string[] = [];
  unidadesMedida: string[] = [];
  categorias: DatosDetalleCategoria[] = [];

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    partida: ['', Validators.required],
    categoriaId: ['', Validators.required],
    unidadMedida: ['', Validators.required],
    codigo: [''],
    precioCompra: [null],
    precioVenta: ['', Validators.required],
  });

  constructor(@Inject(MAT_DIALOG_DATA) public producto: DatosListarProductos) {}

  ngOnInit(): void {
    this.enumsService.getPartidas().subscribe((res) => (this.partidas = res));
    this.enumsService.getUnidadesMedida().subscribe((res) => (this.unidadesMedida = res));
    this.categoriaService.listar().subscribe((res) => (this.categorias = res));
  }

  editar(): void {
    this.cargando = true;
    const catId = this.producto.categoria && this.categorias.length > 0
      ? this.categorias.find(c => c.nombre === this.producto.categoria)?.id ?? ''
      : '';
    this.form.patchValue({
      nombre: this.producto.nombre,
      partida: this.producto.partida,
      categoriaId: catId,
      codigo: this.producto.codigo,
      precioVenta: this.producto.precioVenta,
    });
    this.editMode = true;
    this.cargando = false;
  }

  guardar(): void {
    if (this.form.invalid) return;
    this.cargando = true;
    this.productoService.actualizar(this.producto.id, this.form.value).subscribe({
      next: (actualizado) => {
        this.snackBar.open('Producto actualizado', 'Cerrar', { duration: 3000 });
        this.producto = {
          ...this.producto,
          nombre: actualizado.nombre,
          partida: actualizado.partida,
          codigo: actualizado.codigo,
          precioVenta: actualizado.precioVenta,
        };
        this.editMode = false;
        this.cargando = false;
        this.dialogRef.close('updated');
      },
      error: () => {
        this.snackBar.open('Error al actualizar producto', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  cancelarEdicion(): void {
    this.editMode = false;
    this.form.reset();
  }

  borrar(): void {
    if (confirm(`¿Eliminar producto "${this.producto.nombre}"?`)) {
      this.productoService.eliminar(this.producto.id).subscribe({
        next: () => {
          this.snackBar.open('Producto eliminado', 'Cerrar', { duration: 3000 });
          this.dialogRef.close('deleted');
        },
        error: () => this.snackBar.open('Error al eliminar producto', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cerrar(): void {
    this.dialogRef.close();
  }
}
