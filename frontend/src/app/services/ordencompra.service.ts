import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosRegistroOrdenCompra, DatosListarOrdenCompra, DatosDetalleOrdenCompra, DatosActulizarOrdenCompra } from '../models/ordencompra.model';

@Injectable({ providedIn: 'root' })
export class OrdenCompraService {
  private apiUrl = `${environment.apiUrl}/orden_compra`;

  constructor(private http: HttpClient) {}

  registrar(datos: DatosRegistroOrdenCompra): Observable<DatosDetalleOrdenCompra> {
    return this.http.post<DatosDetalleOrdenCompra>(this.apiUrl, datos);
  }

  listar(page = 0, size = 9): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.apiUrl, { params });
  }

  buscarPorId(id: string): Observable<DatosDetalleOrdenCompra> {
    return this.http.get<DatosDetalleOrdenCompra>(`${this.apiUrl}/${id}`);
  }

  actualizar(id: string, datos: DatosActulizarOrdenCompra): Observable<DatosDetalleOrdenCompra> {
    return this.http.patch<DatosDetalleOrdenCompra>(`${this.apiUrl}/${id}`, datos);
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  confirmar(id: string): Observable<DatosDetalleOrdenCompra> {
    return this.http.post<DatosDetalleOrdenCompra>(`${this.apiUrl}/${id}/confirmar`, {});
  }

  listarNotasPorOrden(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}/notas`);
  }

  generarTodasNotas(id: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${id}/generar-notas`, {});
  }
}
