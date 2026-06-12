import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Factura, DatosRegistroFactura } from '../models/factura.model';

@Injectable({ providedIn: 'root' })
export class FacturaService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/facturas`;

  generar(datos: DatosRegistroFactura): Observable<Factura> {
    return this.http.post<Factura>(this.apiUrl, datos);
  }

  generarExtras(datos: DatosRegistroFactura): Observable<Factura> {
    return this.http.post<Factura>(`${this.apiUrl}/extras`, datos);
  }

  listar(): Observable<Factura[]> {
    return this.http.get<Factura[]>(this.apiUrl);
  }

  obtenerPorId(id: string): Observable<Factura> {
    return this.http.get<Factura>(`${this.apiUrl}/${id}`);
  }

  obtenerPorOrdenCompraId(ordenCompraId: string): Observable<Factura> {
    return this.http.get<Factura>(`${this.apiUrl}/por-orden/${ordenCompraId}`);
  }

  obtenerFacturaExtrasPorOrdenCompraId(ordenCompraId: string): Observable<Factura> {
    return this.http.get<Factura>(`${this.apiUrl}/extras/por-orden/${ordenCompraId}`);
  }
}
