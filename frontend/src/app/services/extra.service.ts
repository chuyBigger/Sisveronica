import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosRegistroExtra, DatosListarExtra } from '../models/extra.model';

@Injectable({ providedIn: 'root' })
export class ExtraService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/extras`;

  crear(datos: DatosRegistroExtra): Observable<DatosListarExtra> {
    return this.http.post<DatosListarExtra>(this.apiUrl, datos);
  }

  listarPorOrden(ordenCompraId: string): Observable<DatosListarExtra[]> {
    return this.http.get<DatosListarExtra[]>(`${this.apiUrl}/orden/${ordenCompraId}`);
  }

  firmar(id: string): Observable<DatosListarExtra> {
    return this.http.post<DatosListarExtra>(`${this.apiUrl}/${id}/firmar`, {});
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
