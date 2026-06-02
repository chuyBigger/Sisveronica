import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UsuarioAdmin {
  id: string;
  username: string;
  role: string;
  activo: boolean;
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
  permisos: PermisoAsignado[];
}

@Injectable({ providedIn: 'root' })
export class UsuarioAdminService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  listar(): Observable<UsuarioAdmin[]> {
    return this.http.get<UsuarioAdmin[]>(this.apiUrl);
  }

  buscar(id: string): Observable<DetalleUsuario> {
    return this.http.get<DetalleUsuario>(`${this.apiUrl}/${id}`);
  }

  crear(datos: { username: string; password: string; role: string }): Observable<any> {
    return this.http.post(this.apiUrl, datos);
  }

  asignarPermisos(usuarioId: string, permisos: { modulo: string; accion: string }[]): Observable<any> {
    return this.http.put(`${this.apiUrl}/${usuarioId}/permisos`, { usuarioId, permisos });
  }

  toggle(usuarioId: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${usuarioId}/toggle`, {});
  }
}
