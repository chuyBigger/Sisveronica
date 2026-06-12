export interface UsuarioAdmin {
  id: string;
  username: string;
  role: string;
  activo: boolean;
  nombreCompleto?: string;
  correo?: string;
  numero?: string;
  cargo?: string;
}

export interface PermisoAsignado {
  modulo: string;
  accion: string;
}

export interface DetalleUsuario {
  id: string;
  username: string;
  role: string;
  activo: boolean;
  nombreCompleto?: string;
  correo?: string;
  numero?: string;
  cargo?: string;
  permisos: PermisoAsignado[];
}
