import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class EnumsService {
  private apiUrl = `${environment.apiUrl}/enums`;

  constructor(private http: HttpClient) {}

  getPartidas(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/partidas`);
  }

  getUnidadesMedida(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/unidades-medida`);
  }
}
