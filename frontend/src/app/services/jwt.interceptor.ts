import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const isAuthError = error.status === 401;
      const isNetworkOrServerError = error.status === 0 || error.status === 503 || error.status === 504;

      if (isAuthError || isNetworkOrServerError) {
        authService.logout();
        const msg = isAuthError ? 'sessionExpired' : 'serverUnavailable';
        router.navigate(['/'], { queryParams: { [msg]: 'true' } });
      }
      return throwError(() => error);
    })
  );
};