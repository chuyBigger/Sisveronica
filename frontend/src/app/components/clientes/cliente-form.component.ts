import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './cliente-form.component.html',
  styleUrl: './cliente-form.component.scss',
})
export class ClienteFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private clienteService = inject(ClienteService);
  private snackBar = inject(MatSnackBar);

  esEdicion = false;
  clienteId: string | null = null;
  cargando = false;

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    rfc: ['', Validators.required],
    calle: [''],
    numero: [null],
    fraccionamiento: [''],
    cp: ['', Validators.required],
    municipio: [''],
    estado: [''],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.esEdicion = true;
      this.clienteId = idParam;
      const cached = this.clienteService.getFromCache(idParam);
      if (cached) {
        this.form.patchValue(cached);
        this.marcarTocados();
      } else {
        this.cargarCliente(idParam);
      }
    }
  }

  cargarCliente(id: string): void {
    this.cargando = true;
    this.clienteService.buscarPorId(id).subscribe({
      next: (cliente) => {
        this.form.patchValue(cliente);
        this.marcarTocados();
        this.cargando = false;
      },
      error: () => {
        this.snackBar.open('Error al cargar cliente', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  private marcarTocados(): void {
    Object.values(this.form.controls).forEach(c => c.markAsTouched());
  }

  guardar(): void {
    this.marcarTocados();
    if (this.form.invalid) return;
    const datos = this.form.value;

    if (this.esEdicion && this.clienteId) {
      this.clienteService.actualizar(this.clienteId, datos).subscribe({
        next: () => {
          this.snackBar.open('Cliente actualizado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/clientes']);
        },
        error: () => this.snackBar.open('Error al actualizar cliente', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.clienteService.registrar(datos).subscribe({
        next: () => {
          this.snackBar.open('Cliente creado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/clientes']);
        },
        error: () => this.snackBar.open('Error al crear cliente', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/clientes']);
  }
}
