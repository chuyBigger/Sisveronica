import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NotaVentaService } from '../../services/notaventa.service';
import { DatosDetalleNota } from '../../models/notaventa.model';
import { DetalleDialogComponent, DetalleDialogData } from './detalle-dialog.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-notaventa-detalle',
  standalone: true,
  imports: [
    CommonModule, RouterModule,
    MatButtonModule, MatIconModule, MatCardModule,
    MatSnackBarModule, MatProgressSpinnerModule, MatDividerModule, MatDialogModule, MatTooltipModule,
  ],
  templateUrl: './notaventa-detalle.component.html',
  styleUrl: './notaventa-detalle.component.scss',
})
export class NotaVentaDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private notaventaService = inject(NotaVentaService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  nota: DatosDetalleNota | null = null;
  cargando = true;

  readonly negocio = {
    nombre: 'CARNICERÍA "LA VERÓNICA"',
    eslogan: 'La Mejor Calidad',
    fiscal: 'Jesus Manuel Romo Alba',
    rfc: 'R.F.C. ROAJ600629RQ5',
    domicilio: 'C. ALEGRIA #211 BARRIO DEL ENCINO C. P. 20240, Aguascalientes, Ags.',
  };

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.router.navigate(['/notaventas']);
      return;
    }
    this.notaventaService.buscarPorId(id).subscribe({
      next: (nota) => {
        this.nota = nota;
        this.cargando = false;
      },
      error: () => {
        this.snackBar.open('Error al cargar la nota', 'Cerrar', { duration: 3000 });
        this.cargando = false;
        this.router.navigate(['/notaventas']);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/notaventas']);
  }

  editar(): void {
    if (this.nota) {
      this.router.navigate(['/notaventas', this.nota.id]);
    }
  }

  borrar(): void {
    if (!this.nota) return;
    if (confirm(`¿Eliminar nota Folio #${this.nota.folio}?`)) {
      this.notaventaService.eliminar(this.nota.id).subscribe({
        next: () => {
          this.snackBar.open('Nota eliminada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/notaventas']);
        },
        error: () => this.snackBar.open('Error al eliminar nota', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  imprimir(): void {
    const count = this.nota?.detalles?.length ?? 0;
    const size = count > 8 ? '108mm 186mm' : '108mm 140mm';
    const fontSize = count > 10 ? 8 : count > 6 ? 9 : 10;

    const style = document.createElement('style');
    style.id = 'print-size';
    style.textContent = `@page { size: ${size}; margin: 0.6cm; }
#print-area, #print-area * { font-size: ${fontSize}px !important; }`;
    document.head.appendChild(style);

    document.body.classList.add('printing-detalle');
    window.print();

    const cleanup = () => {
      document.body.classList.remove('printing-detalle');
      document.getElementById('print-size')?.remove();
      window.removeEventListener('afterprint', cleanup);
    };
    window.addEventListener('afterprint', cleanup);
  }

  firmarNota(): void {
    if (!this.nota) return;
    this.notaventaService.firmar(this.nota.id).subscribe({
      next: () => {
        if (this.nota) this.nota.firmada = true;
        this.snackBar.open('Nota firmada', 'Cerrar', { duration: 2000 });
      },
      error: () => this.snackBar.open('Error al firmar nota', 'Cerrar', { duration: 3000 }),
    });
  }

  editarDetalle(): void {
    if (!this.nota) return;
    const dialogRef = this.dialog.open(DetalleDialogComponent, {
      width: '500px',
      data: { detalleActual: this.nota.detalle, folio: this.nota.folio } as DetalleDialogData,
    });
    dialogRef.afterClosed().subscribe((detalle) => {
      if (detalle !== undefined && this.nota) {
        this.notaventaService.actualizarDetalle(this.nota.id, detalle).subscribe({
          next: () => {
            if (this.nota) this.nota.detalle = detalle;
            this.snackBar.open('Detalle actualizado', 'Cerrar', { duration: 2000 });
          },
          error: () => this.snackBar.open('Error al actualizar detalle', 'Cerrar', { duration: 3000 }),
        });
      }
    });
  }
}
