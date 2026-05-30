import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  DatosRegistroProducto,
  DatosListarProductos,
  DatosDetalleProducto,
  DatosActualizarProducto,
} from '../models/producto.model';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private apiUrl = `${environment.apiUrl}/productos`;

  constructor(private http: HttpClient) {}

  registrar(datos: DatosRegistroProducto): Observable<DatosDetalleProducto> {
    return this.http.post<DatosDetalleProducto>(this.apiUrl, datos);
  }

  listar(page = 0, size = 9): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.apiUrl, { params });
  }

  listarPorPartida(partida: string, page = 0, size = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/partidas/${partida}`, { params });
  }

  listarPorCategoria(id: number, page = 0, size = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/categorias/${id}`, { params });
  }

  buscarPorId(id: number): Observable<DatosDetalleProducto> {
    return this.http.get<DatosDetalleProducto>(`${this.apiUrl}/${id}`);
  }

  buscarPorNombre(nombre: string): Observable<DatosDetalleProducto> {
    return this.http.get<DatosDetalleProducto>(`${this.apiUrl}/buscar/${nombre}`);
  }

  buscarPorPalabra(q: string, page = 0, size = 10): Observable<any> {
    const params = new HttpParams().set('q', q).set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/buscar_palabras`, { params });
  }

  actualizar(id: number, datos: DatosActualizarProducto): Observable<DatosDetalleProducto> {
    return this.http.patch<DatosDetalleProducto>(`${this.apiUrl}/${id}`, datos);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
