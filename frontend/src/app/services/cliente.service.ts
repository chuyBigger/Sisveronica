import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosRegistroCliente, DatosDetalleCliente, DatosActualizarCliente } from '../models/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  private apiUrl = `${environment.apiUrl}/clientes`;

  constructor(private http: HttpClient) {}

  registrar(datos: DatosRegistroCliente): Observable<any> {
    return this.http.post(this.apiUrl, datos);
  }

  listar(): Observable<DatosDetalleCliente[]> {
    return this.http.get<DatosDetalleCliente[]>(this.apiUrl);
  }

  buscarPorId(id: string): Observable<DatosDetalleCliente> {
    return this.http.get<DatosDetalleCliente>(`${this.apiUrl}/${id}`);
  }

  actualizar(id: string, datos: DatosActualizarCliente): Observable<DatosDetalleCliente> {
    return this.http.patch<DatosDetalleCliente>(`${this.apiUrl}/${id}`, datos);
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
