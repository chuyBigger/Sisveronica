import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { CategoriaService } from '../../services/categoria.service';
import { EnumsService } from '../../services/enums.service';
import { DatosDetalleCategoria } from '../../models/categoria.model';

@Component({
  selector: 'app-categoria-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatSnackBarModule,
    MatDialogModule,
    MatDividerModule,
  ],
  template: `
    <h2 mat-dialog-title>Administrar Categorías</h2>
    <mat-dialog-content>
      <mat-card class="add-card">
        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="guardar()" class="add-form">
            <mat-form-field appearance="outline" subscriptSizing="dynamic">
              <mat-label>Nombre</mat-label>
              <input matInput formControlName="nombre" placeholder="NUEVA CATEGORÍA"
                     style="text-transform: uppercase"
                     (input)="onNombreInput($event)">
              @if (form.get('nombre')?.hasError('required')) {
                <mat-error>Requerido</mat-error>
              }
            </mat-form-field>
            <mat-form-field appearance="outline" subscriptSizing="dynamic">
              <mat-label>Partida</mat-label>
              <mat-select formControlName="partida">
                @for (p of partidas; track p) {
                  <mat-option [value]="p">{{ p }}</mat-option>
                }
              </mat-select>
              @if (form.get('partida')?.hasError('required')) {
                <mat-error>Requerido</mat-error>
              }
            </mat-form-field>
            <button mat-raised-button color="primary" type="submit"
                    [disabled]="form.invalid || editandoId !== null">
              <mat-icon>add</mat-icon> Agregar
            </button>
          </form>
        </mat-card-content>
      </mat-card>

      <mat-divider class="divider"></mat-divider>

      @if (editandoId) {
        <mat-card class="edit-card">
          <mat-card-content>
            <form [formGroup]="editForm" (ngSubmit)="actualizar()" class="add-form">
              <mat-form-field appearance="outline" subscriptSizing="dynamic">
                <mat-label>Nombre</mat-label>
                <input matInput formControlName="nombre" placeholder="CATEGORÍA"
                       style="text-transform: uppercase"
                       (input)="onEditNombreInput($event)">
                @if (editForm.get('nombre')?.hasError('required')) {
                  <mat-error>Requerido</mat-error>
                }
              </mat-form-field>
              <mat-form-field appearance="outline" subscriptSizing="dynamic">
                <mat-label>Partida</mat-label>
                <mat-select formControlName="partida">
                  @for (p of partidas; track p) {
                    <mat-option [value]="p">{{ p }}</mat-option>
                  }
                </mat-select>
                @if (editForm.get('partida')?.hasError('required')) {
                  <mat-error>Requerido</mat-error>
                }
              </mat-form-field>
              <button mat-raised-button color="primary" type="submit" [disabled]="editForm.invalid">
                <mat-icon>save</mat-icon> Actualizar
              </button>
              <button mat-button type="button" (click)="cancelarEdicion()">Cancelar</button>
            </form>
          </mat-card-content>
        </mat-card>
      }

      <div class="lista">
        @if (categorias.length === 0) {
          <p class="sin-datos">No hay categorías registradas.</p>
        }
        @for (cat of categorias; track cat.id) {
          <div class="categoria-row">
            <div class="cat-info">
              <span class="cat-nombre">{{ cat.nombre }}</span>
              <span class="cat-partida">{{ cat.partida }}</span>
            </div>
            <div class="cat-acciones">
              <button mat-icon-button color="primary" (click)="editar(cat)" matTooltip="Editar">
                <mat-icon>edit</mat-icon>
              </button>
              <button mat-icon-button color="warn" (click)="eliminar(cat)" matTooltip="Eliminar">
                <mat-icon>delete</mat-icon>
              </button>
            </div>
          </div>
        }
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cerrar</button>
    </mat-dialog-actions>
  `,
  styles: `
    .add-card, .edit-card {
      margin-bottom: 12px;
    }
    .add-form {
      display: flex;
      align-items: flex-start;
      gap: 12px;
      flex-wrap: wrap;
    }
    .add-form mat-form-field {
      flex: 1;
      min-width: 150px;
    }
    .add-form button {
      margin-top: 4px;
    }
    .divider {
      margin: 16px 0;
    }
    .lista {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }
    .categoria-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 8px 12px;
      border-radius: 4px;
      transition: background 0.15s;
    }
    .categoria-row:hover {
      background: #f5f5f5;
    }
    .cat-info {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .cat-nombre {
      font-weight: 500;
      font-size: 0.95rem;
    }
    .cat-partida {
      color: #888;
      font-size: 0.8rem;
      background: #f0f0f0;
      padding: 2px 8px;
      border-radius: 4px;
    }
    .cat-acciones {
      display: flex;
      gap: 4px;
    }
    .sin-datos {
      color: #888;
      text-align: center;
      padding: 20px;
      font-style: italic;
    }
    :host-context(body.dark-mode) {
      .categoria-row:hover {
        background: #2a2a3e;
      }
      .cat-partida {
        background: #333;
        color: #aaa;
      }
    }
  `,
})
export class CategoriaDialogComponent implements OnInit {
  private fb = inject(FormBuilder);
  private categoriaService = inject(CategoriaService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<CategoriaDialogComponent>);

  categorias: DatosDetalleCategoria[] = [];
  partidas: string[] = [];
  editandoId: string | null = null;

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    partida: ['', Validators.required],
  });

  editForm: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    partida: ['', Validators.required],
  });

  ngOnInit(): void {
    this.cargarPartidas();
    this.cargarCategorias();
  }

  onNombreInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.toUpperCase();
    this.form.patchValue({ nombre: input.value });
  }

  onEditNombreInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.toUpperCase();
    this.editForm.patchValue({ nombre: input.value });
  }

  cargarPartidas(): void {
    this.enumsService.getPartidas().subscribe(res => this.partidas = res);
  }

  cargarCategorias(): void {
    this.categoriaService.listar().subscribe(res => this.categorias = res);
  }

  guardar(): void {
    if (this.form.invalid) return;
    const datos = { ...this.form.value, nombre: this.form.value.nombre.toUpperCase().trim() };
    this.categoriaService.registrar(datos).subscribe({
      next: () => {
        this.snackBar.open('Categoría creada', 'Cerrar', { duration: 2000 });
        this.form.reset();
        this.cargarCategorias();
      },
      error: (err) => {
        const msg = err.error?.message || err.error || 'Error al crear categoría';
        this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
      },
    });
  }

  editar(cat: DatosDetalleCategoria): void {
    this.editandoId = cat.id;
    this.editForm.patchValue({ nombre: cat.nombre, partida: cat.partida });
  }

  cancelarEdicion(): void {
    this.editandoId = null;
    this.editForm.reset();
  }

  actualizar(): void {
    if (this.editForm.invalid || !this.editandoId) return;
    const datos = { ...this.editForm.value, nombre: this.editForm.value.nombre.toUpperCase().trim() };
    this.categoriaService.actualizar(this.editandoId, datos).subscribe({
      next: () => {
        this.snackBar.open('Categoría actualizada', 'Cerrar', { duration: 2000 });
        this.cancelarEdicion();
        this.cargarCategorias();
      },
      error: (err) => {
        const msg = err.error?.message || err.error || 'Error al actualizar';
        this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
      },
    });
  }

  eliminar(cat: DatosDetalleCategoria): void {
    if (!confirm(`¿Eliminar la categoría "${cat.nombre}"?`)) return;
    this.categoriaService.eliminar(cat.id).subscribe({
      next: () => {
        this.snackBar.open('Categoría eliminada', 'Cerrar', { duration: 2000 });
        this.cargarCategorias();
      },
      error: () => this.snackBar.open('Error al eliminar categoría', 'Cerrar', { duration: 3000 }),
    });
  }
}
