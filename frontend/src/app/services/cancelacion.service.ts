import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosRegistroCancelacion, DatosListarCancelacion } from '../models/cancelacion.model';

@Injectable({ providedIn: 'root' })
export class CancelacionService {
  private apiUrl = `${environment.apiUrl}/cancelaciones`;

  constructor(private http: HttpClient) {}

  crear(datos: DatosRegistroCancelacion): Observable<DatosListarCancelacion> {
    return this.http.post<DatosListarCancelacion>(this.apiUrl, datos);
  }

  listarPorOrden(ordenCompraId: string): Observable<DatosListarCancelacion[]> {
    return this.http.get<DatosListarCancelacion[]>(`${this.apiUrl}/orden/${ordenCompraId}`);
  }

  validar(id: string): Observable<DatosListarCancelacion> {
    return this.http.post<DatosListarCancelacion>(`${this.apiUrl}/${id}/validar`, {});
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  reconstruirNotas(ordenCompraId: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/reconstruir/${ordenCompraId}`, {});
  }
}
