import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ProductoReporte {
  productoNombre: string;
  cantidad: number;
  unidadMedida: string;
}

export interface DiaReporte {
  dia: string;
  fecha: string;
  productos: ProductoReporte[];
  totalDia: number;
}

export interface ClienteReporte {
  clienteNombre: string;
  totalGeneral: number;
  dias: DiaReporte[];
}

export interface ReporteProduccionCarne {
  semanaInicio: string;
  semanaFin: string;
  clientes: ClienteReporte[];
}

@Injectable({ providedIn: 'root' })
export class ReporteProduccionService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/reportes`;

  obtenerReporte(semana: string): Observable<ReporteProduccionCarne> {
    const params = new HttpParams().set('semana', semana);
    return this.http.get<ReporteProduccionCarne>(`${this.apiUrl}/produccion-carne`, { params });
  }
}
