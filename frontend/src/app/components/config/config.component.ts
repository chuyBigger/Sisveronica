import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { UsuarioAdminService } from '../../services/usuario-admin.service';
import { EnumsService } from '../../services/enums.service';
import { UsuarioAdmin, DetalleUsuario } from '../../models/usuario.model';

@Component({
  selector: 'app-config',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatCardModule, MatIconModule, MatButtonModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatDividerModule, MatSnackBarModule, MatTooltipModule, MatTabsModule, MatCheckboxModule,
  ],
  templateUrl: './config.component.html',
  styleUrl: './config.component.scss',
})
export class ConfigComponent implements OnInit {
  private usuarioAdminService = inject(UsuarioAdminService);
  private enumsService = inject(EnumsService);
  private snackBar = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);

  usuarios: UsuarioAdmin[] = [];
  selectedUser: UsuarioAdmin | null = null;
  userDetail: DetalleUsuario | null = null;

  modulos: string[] = [];
  acciones: string[] = ['CREAR', 'LEER', 'ACTUALIZAR', 'ELIMINAR'];

  // New user form
  formNombre = '';
  formUsername = '';
  formCorreo = '';
  formNumero = '';
  formPassword = '';
  formCargo = '';
  formRole = 'USER';

  // Edit mode
  editNombre = '';
  editCorreo = '';
  editNumero = '';
  editCargo = '';
  editPassword = '';
  editRole = '';

  permisosMatrix: { modulo: string; checks: { accion: string; activo: boolean }[] }[] = [];

  ngOnInit(): void {
    this.cargarUsuarios();
    this.enumsService.getModulos().subscribe({
      next: (res) => {
        this.modulos = res;
        this.iniciarMatriz();
      },
    });
  }

  iniciarMatriz(): void {
    this.permisosMatrix = this.modulos.map((m) => ({
      modulo: m,
      checks: this.acciones.map((a) => ({ accion: a, activo: false })),
    }));
  }

  cargarUsuarios(): void {
    this.usuarioAdminService.listar().subscribe({
      next: (res) => {
        this.usuarios = res;
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar usuarios', 'Cerrar', { duration: 3000 }),
    });
  }

  seleccionarUsuario(u: UsuarioAdmin): void {
    this.selectedUser = u;
    this.editNombre = u.nombreCompleto || '';
    this.editCorreo = u.correo || '';
    this.editNumero = u.numero || '';
    this.editCargo = u.cargo || '';
    this.editPassword = '';
    this.editRole = u.role;

    this.usuarioAdminService.buscar(u.id).subscribe({
      next: (det) => {
        this.userDetail = det;
        this.cargarPermisosEnMatriz(det.permisos);
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar detalle del usuario', 'Cerrar', { duration: 3000 }),
    });
  }

  cargarPermisosEnMatriz(permisos: { modulo: string; accion: string }[]): void {
    this.iniciarMatriz();
    for (const p of permisos) {
      const row = this.permisosMatrix.find((r) => r.modulo === p.modulo);
      if (row) {
        const cell = row.checks.find((c) => c.accion === p.accion);
        if (cell) cell.activo = true;
      }
    }
  }

  getPermisosSeleccionados(): { modulo: string; accion: string }[] {
    const result: { modulo: string; accion: string }[] = [];
    for (const row of this.permisosMatrix) {
      for (const c of row.checks) {
        if (c.activo) result.push({ modulo: row.modulo, accion: c.accion });
      }
    }
    return result;
  }

  guardarPermisos(): void {
    if (!this.selectedUser) return;
    const permisos = this.getPermisosSeleccionados();
    this.usuarioAdminService.asignarPermisos(this.selectedUser.id, permisos).subscribe({
      next: () => {
        this.snackBar.open('Permisos guardados', 'Cerrar', { duration: 2000 });
      },
      error: () => this.snackBar.open('Error al guardar permisos', 'Cerrar', { duration: 3000 }),
    });
  }

  guardarPerfil(): void {
    if (!this.selectedUser) return;
    const datos: any = {};
    if (this.editNombre) datos.nombreCompleto = this.editNombre;
    if (this.editCorreo) datos.correo = this.editCorreo;
    if (this.editNumero) datos.numero = this.editNumero;
    if (this.editCargo) datos.cargo = this.editCargo;
    if (this.editPassword) datos.password = this.editPassword;
    if (this.editRole) datos.role = this.editRole;

    this.usuarioAdminService.actualizar(this.selectedUser.id, datos).subscribe({
      next: (res) => {
        this.snackBar.open('Perfil actualizado', 'Cerrar', { duration: 2000 });
        Object.assign(this.selectedUser!, res);
        this.cargarUsuarios();
      },
      error: () => this.snackBar.open('Error al actualizar perfil', 'Cerrar', { duration: 3000 }),
    });
  }

  toggleUsuario(u: UsuarioAdmin): void {
    this.usuarioAdminService.toggle(u.id).subscribe({
      next: () => {
        u.activo = !u.activo;
        this.snackBar.open(`Usuario ${u.activo ? 'activado' : 'desactivado'}`, 'Cerrar', { duration: 2000 });
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cambiar estado', 'Cerrar', { duration: 3000 }),
    });
  }

  crearUsuario(): void {
    if (!this.formUsername || !this.formPassword) {
      this.snackBar.open('Usuario y contraseña son obligatorios', 'Cerrar', { duration: 3000 });
      return;
    }
    const datos: any = {
      username: this.formUsername,
      password: this.formPassword,
      role: this.formRole,
    };
    if (this.formNombre) datos.nombreCompleto = this.formNombre;
    if (this.formCorreo) datos.correo = this.formCorreo;
    if (this.formNumero) datos.numero = this.formNumero;
    if (this.formCargo) datos.cargo = this.formCargo;

    this.usuarioAdminService.crear(datos).subscribe({
      next: () => {
        this.snackBar.open('Usuario creado', 'Cerrar', { duration: 2000 });
        this.limpiarFormulario();
        this.cargarUsuarios();
      },
      error: (err) =>
        this.snackBar.open(err.error?.error || 'Error al crear usuario', 'Cerrar', { duration: 3000 }),
    });
  }

  limpiarFormulario(): void {
    this.formNombre = '';
    this.formUsername = '';
    this.formCorreo = '';
    this.formNumero = '';
    this.formPassword = '';
    this.formCargo = '';
    this.formRole = 'USER';
  }

  nuevoUsuario(): void {
    this.selectedUser = null;
    this.userDetail = null;
    this.limpiarFormulario();
  }

  tienePermiso(modulo: string, accion: string): boolean {
    const row = this.permisosMatrix.find((r) => r.modulo === modulo);
    if (!row) return false;
    const cell = row.checks.find((c) => c.accion === accion);
    return cell?.activo ?? false;
  }

  nombreModulo(mod: string): string {
    const labels: Record<string, string> = {
      PRODUCTOS: 'Productos',
      CLIENTES: 'Clientes',
      CONTRATOS: 'Contratos',
      NOTAS_VENTA: 'Notas de Venta',
      ORDENES_COMPRA: 'Órdenes de Compra',
      REPORTES: 'Reportes',
      USUARIOS: 'Usuarios',
    };
    return labels[mod] || mod;
  }

  nombreAccion(acc: string): string {
    const labels: Record<string, string> = {
      CREAR: 'Crear',
      LEER: 'Leer',
      ACTUALIZAR: 'Actualizar',
      ELIMINAR: 'Eliminar',
    };
    return labels[acc] || acc;
  }
}
