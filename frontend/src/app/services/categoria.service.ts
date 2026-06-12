import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosRegistroCategoria, DatosDetalleCategoria, DatosActualizarCategoria } from '../models/categoria.model';

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private apiUrl = `${environment.apiUrl}/categorias`;
  private cache: DatosDetalleCategoria[] = [];

  constructor(private http: HttpClient) {}

  registrar(datos: DatosRegistroCategoria): Observable<any> {
    return this.http.post(this.apiUrl, datos);
  }

  listar(): Observable<DatosDetalleCategoria[]> {
    return this.http.get<DatosDetalleCategoria[]>(this.apiUrl).pipe(
      tap(cats => this.cache = cats)
    );
  }

  getFromCache(id: string): DatosDetalleCategoria | undefined {
    return this.cache.find(c => c.id === id);
  }

  buscarPorId(id: string): Observable<DatosDetalleCategoria> {
    return this.http.get<DatosDetalleCategoria>(`${this.apiUrl}/${id}`);
  }

  actualizar(id: string, datos: DatosActualizarCategoria): Observable<DatosDetalleCategoria> {
    return this.http.patch<DatosDetalleCategoria>(`${this.apiUrl}/${id}`, datos);
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
