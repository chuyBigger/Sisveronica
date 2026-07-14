import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ReporteCargaProductos {
  totalProcesados: number;
  exitosos: number;
  duplicados: number;
  sinPrecio: number;
  mensajesDuplicados: string[];
  mensajesSinPrecio: string[];
}

@Injectable({ providedIn: 'root' })
export class ProductoExcelService {
  private apiUrl = `${environment.apiUrl}/productos/excel`;

  constructor(private http: HttpClient) {}

  cargarProductos(archivo: File): Observable<ReporteCargaProductos> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<ReporteCargaProductos>(`${this.apiUrl}/cargar`, formData);
  }

  descargarPlantilla(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/plantilla`, {
      responseType: 'blob',
    });
  }

  exportarProductos(partida: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/exportar`, {
      params: { partida },
      responseType: 'blob',
    });
  }
}
