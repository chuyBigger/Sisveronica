import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosRegistroNota, DatosListarNota, DatosDetalleNota, DatosActualizarNota } from '../models/notaventa.model';

@Injectable({ providedIn: 'root' })
export class NotaVentaService {
  private apiUrl = `${environment.apiUrl}/notaventas`;

  constructor(private http: HttpClient) {}

  registrar(datos: DatosRegistroNota): Observable<DatosDetalleNota> {
    return this.http.post<DatosDetalleNota>(this.apiUrl, datos);
  }

  generarDesdeOrden(ordenCompraId: string, dia: string): Observable<DatosDetalleNota> {
    return this.http.post<DatosDetalleNota>(`${this.apiUrl}/generar-desde-orden`, { ordenCompraId, dia });
  }

  listar(page = 0, size = 9): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.apiUrl, { params });
  }

  buscarPorId(id: string): Observable<DatosDetalleNota> {
    return this.http.get<DatosDetalleNota>(`${this.apiUrl}/${id}`);
  }

  actualizar(id: string, datos: DatosActualizarNota): Observable<DatosDetalleNota> {
    return this.http.patch<DatosDetalleNota>(`${this.apiUrl}/${id}`, datos);
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
