import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-super-admin',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDividerModule,
  ],
  templateUrl: './super-admin.component.html',
  styleUrl: './super-admin.component.scss',
})
export class SuperAdminComponent {
  private adminService = inject(AdminService);
  private snackBar = inject(MatSnackBar);

  entityType = 'nota';
  inputValue = '';
  deleting = false;
  result: string | null = null;

  eliminar(): void {
    const val = this.inputValue.trim();
    if (!val) {
      this.snackBar.open('Ingresa un ID o folio', 'Cerrar', { duration: 3000 });
      return;
    }

    const msg = this.entityType === 'nota'
      ? `¿Eliminar NOTA con${/^\d+$/.test(val) ? ' folio' : ' ID'} "${val}"?`
      : `¿Eliminar ${this.entityType === 'orden' ? 'ORDEN' : 'CANCELACIÓN'} con ID "${val}"?`;

    const advertencia = 'Esto la desactivará permanentemente. NO se podrá recuperar y los folios se perderán.';

    if (!confirm(`${msg}\n\n${advertencia}`)) return;

    this.deleting = true;
    this.result = null;

    const req = this.buildRequest(val);
    req.subscribe({
      next: (res) => {
        this.result = res.mensaje;
        this.snackBar.open(res.mensaje, 'Cerrar', { duration: 4000 });
        this.inputValue = '';
        this.deleting = false;
      },
      error: (err) => {
        this.result = err.error?.error || err.error?.mensaje || 'Error al eliminar';
        this.snackBar.open(this.result!, 'Cerrar', { duration: 5000 });
        this.deleting = false;
      },
    });
  }

  private buildRequest(val: string) {
    switch (this.entityType) {
      case 'nota':
        if (/^\d+$/.test(val)) {
          return this.adminService.eliminarNotaPorFolio(Number(val));
        }
        return this.adminService.eliminarNotaPorId(val);
      case 'orden':
        return this.adminService.eliminarOrdenPorId(val);
      case 'cancelacion':
        return this.adminService.eliminarCancelacionPorId(val);
      default:
        throw new Error('Tipo no válido');
    }
  }
}
