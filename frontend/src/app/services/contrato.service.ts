import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosRegistroContrato, DatosDetalleContrato, DatosActualizarContrato } from '../models/contrato.model';

@Injectable({ providedIn: 'root' })
export class ContratoService {
  private apiUrl = `${environment.apiUrl}/contratos`;
  private cache: DatosDetalleContrato[] = [];

  constructor(private http: HttpClient) {}

  registrar(datos: DatosRegistroContrato): Observable<DatosDetalleContrato> {
    return this.http.post<DatosDetalleContrato>(this.apiUrl, datos);
  }

  listar(): Observable<DatosDetalleContrato[]> {
    return this.http.get<DatosDetalleContrato[]>(this.apiUrl).pipe(
      tap(contratos => this.cache = contratos)
    );
  }

  getFromCache(id: string): DatosDetalleContrato | undefined {
    return this.cache.find(c => c.id === id);
  }

  buscarPorId(id: string): Observable<DatosDetalleContrato> {
    return this.http.get<DatosDetalleContrato>(`${this.apiUrl}/${id}`);
  }

  actualizar(id: string, datos: DatosActualizarContrato): Observable<DatosDetalleContrato> {
    return this.http.patch<DatosDetalleContrato>(`${this.apiUrl}/${id}`, datos);
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
