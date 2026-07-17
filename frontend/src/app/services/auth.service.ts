import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { DatosLogin, DatosRegistroUsuario, DatosRespuestaAuth } from '../models/auth.model';

interface JwtPayload {
  exp: number;
  role?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<DatosRespuestaAuth | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    const stored = localStorage.getItem('auth_token');
    if (stored && !this.isTokenExpired(stored)) {
      const user = localStorage.getItem('auth_user');
      if (user) {
        this.currentUserSubject.next(JSON.parse(user));
      }
    } else if (stored) {
      this.logout();
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1])) as JwtPayload;
      return Date.now() >= payload.exp * 1000;
    } catch {
      return true;
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
    const token = localStorage.getItem('auth_token');
    if (token && this.isTokenExpired(token)) {
      this.logout();
      return null;
    }
    return token;
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
