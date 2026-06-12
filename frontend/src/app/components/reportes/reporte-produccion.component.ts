import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ReporteProduccionService, ReporteProduccionCarne } from './reporte-produccion.service';

@Component({
  selector: 'app-reporte-produccion',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatDatepickerModule, MatNativeDateModule,
    MatButtonModule, MatIconModule, MatSnackBarModule,
  ],
  templateUrl: './reporte-produccion.component.html',
  styleUrl: './reporte-produccion.component.scss',
})
export class ReporteProduccionComponent {
  private service = inject(ReporteProduccionService);
  private snackBar = inject(MatSnackBar);

  selectedDate: Date | null = null;
  reporte: ReporteProduccionCarne | null = null;
  loading = false;

  getMonday(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    d.setDate(diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  formatDate(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

  consultar(): void {
    if (!this.selectedDate) {
      this.snackBar.open('Selecciona una fecha', 'Cerrar', { duration: 2000 });
      return;
    }
    const monday = this.getMonday(this.selectedDate);
    this.loading = true;
    this.service.obtenerReporte(this.formatDate(monday)).subscribe({
      next: (r) => {
        this.reporte = r;
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Error al generar reporte', 'Cerrar', { duration: 3000 });
        this.loading = false;
      },
    });
  }

  diaLabel(dia: string): string {
    const map: Record<string, string> = {
      martes: 'Martes', miercoles: 'Miércoles', jueves: 'Jueves',
      viernes: 'Viernes', sabado: 'Sábado', domingo: 'Domingo', lunes: 'Lunes',
    };
    return map[dia] || dia;
  }

  imprimir(): void {
    const style = document.createElement('style');
    style.textContent = `
      @media print {
        body * { visibility: hidden; }
        #reporte-print-area, #reporte-print-area * { visibility: visible; }
        #reporte-print-area { position: absolute; left: 0; top: 0; width: 100%; }
        .no-print { display: none !important; }
        .cliente-page { page-break-after: always; }
        .cliente-page:last-child { page-break-after: auto; }
      }
    `;
    document.head.appendChild(style);
    document.body.classList.add('printing-reporte');
    setTimeout(() => {
      window.print();
      document.body.classList.remove('printing-reporte');
      document.head.removeChild(style);
    }, 300);
  }
}
