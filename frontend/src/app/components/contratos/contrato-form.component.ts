import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ContratoService } from '../../services/contrato.service';
import { ClienteService } from '../../services/cliente.service';
import { DatosDetalleCliente } from '../../models/cliente.model';

@Component({
  selector: 'app-contrato-form',
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
    MatDatepickerModule,
    MatNativeDateModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './contrato-form.component.html',
  styleUrl: './contrato-form.component.scss',
})
export class ContratoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private contratoService = inject(ContratoService);
  private clienteService = inject(ClienteService);
  private snackBar = inject(MatSnackBar);

  esEdicion = false;
  contratoId: string | null = null;
  cargando = false;
  clientes: DatosDetalleCliente[] = [];

  form: FormGroup = this.fb.group({
    contrato: ['', Validators.required],
    clienteId: ['', Validators.required],
    fechaInicio: ['', Validators.required],
    fechaTermino: ['', Validators.required],
    presupuesto: ['', Validators.required],
  });

  private marcarTocados(): void {
    Object.values(this.form.controls).forEach(c => c.markAsTouched());
  }

  ngOnInit(): void {
    this.clienteService.listar().subscribe((res) => (this.clientes = res));
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.esEdicion = true;
      this.contratoId = idParam;
      const cached = this.contratoService.getFromCache(idParam);
      if (cached) {
        this.form.patchValue({
          ...cached,
          fechaInicio: new Date(cached.fechaInicio),
          fechaTermino: new Date(cached.fechaTermino),
        });
        this.marcarTocados();
      } else {
        this.cargarContrato(idParam);
      }
    }
  }

  cargarContrato(id: string): void {
    this.cargando = true;
    this.contratoService.buscarPorId(id).subscribe({
      next: (c) => {
        this.form.patchValue({
          ...c,
          fechaInicio: new Date(c.fechaInicio),
          fechaTermino: new Date(c.fechaTermino),
        });
        this.marcarTocados();
        this.cargando = false;
      },
      error: () => {
        this.snackBar.open('Error al cargar contrato', 'Cerrar', { duration: 3000 });
        this.cargando = false;
      },
    });
  }

  guardar(): void {
    this.marcarTocados();
    if (this.form.invalid) return;
    const raw = this.form.value;
    const datos = {
      ...raw,
      fechaInicio: raw.fechaInicio instanceof Date ? raw.fechaInicio.toISOString().split('T')[0] : raw.fechaInicio,
      fechaTermino: raw.fechaTermino instanceof Date ? raw.fechaTermino.toISOString().split('T')[0] : raw.fechaTermino,
    };

    if (this.esEdicion && this.contratoId) {
      this.contratoService.actualizar(this.contratoId, datos).subscribe({
        next: () => {
          this.snackBar.open('Contrato actualizado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/contratos']);
        },
        error: () => this.snackBar.open('Error al actualizar contrato', 'Cerrar', { duration: 3000 }),
      });
    } else {
      this.contratoService.registrar(datos).subscribe({
        next: () => {
          this.snackBar.open('Contrato creado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/contratos']);
        },
        error: () => this.snackBar.open('Error al crear contrato', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/contratos']);
  }
}
