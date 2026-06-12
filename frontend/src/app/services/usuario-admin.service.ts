import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioAdmin, DetalleUsuario } from '../models/usuario.model';

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

  crear(datos: {
    username: string;
    password: string;
    role: string;
    nombreCompleto?: string;
    correo?: string;
    numero?: string;
    cargo?: string;
  }): Observable<any> {
    return this.http.post(this.apiUrl, datos);
  }

  actualizar(
    id: string,
    datos: {
      nombreCompleto?: string;
      correo?: string;
      numero?: string;
      cargo?: string;
      password?: string;
      role?: string;
    }
  ): Observable<UsuarioAdmin> {
    return this.http.patch<UsuarioAdmin>(`${this.apiUrl}/${id}`, datos);
  }

  asignarPermisos(
    usuarioId: string,
    permisos: { modulo: string; accion: string }[]
  ): Observable<any> {
    return this.http.put(`${this.apiUrl}/${usuarioId}/permisos`, {
      usuarioId,
      permisos,
    });
  }

  toggle(usuarioId: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${usuarioId}/toggle`, {});
  }
}
