import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosLogin, DatosRegistroUsuario, DatosRespuestaAuth } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<DatosRespuestaAuth | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    const stored = localStorage.getItem('auth_token');
    if (stored) {
      const token = stored;
      const user = localStorage.getItem('auth_user');
      if (user) {
        this.currentUserSubject.next(JSON.parse(user));
      }
    }
  }

  login(datos: DatosLogin): Observable<DatosRespuestaAuth> {
    return this.http.post<DatosRespuestaAuth>(`${this.apiUrl}/login`, datos).pipe(
      tap((res) => {
        localStorage.setItem('auth_token', res.token);
        localStorage.setItem('auth_user', JSON.stringify(res));
        this.currentUserSubject.next(res);
      })
    );
  }

  register(datos: DatosRegistroUsuario): Observable<DatosRespuestaAuth> {
    return this.http.post<DatosRespuestaAuth>(`${this.apiUrl}/register`, datos).pipe(
      tap((res) => {
        localStorage.setItem('auth_token', res.token);
        localStorage.setItem('auth_user', JSON.stringify(res));
        this.currentUserSubject.next(res);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  get currentUser(): DatosRespuestaAuth | null {
    return this.currentUserSubject.value;
  }

  hasRole(role: string): boolean {
    return this.currentUser?.role === role;
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }
}
