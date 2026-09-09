import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);

  hidePassword = true;
  cargando = false;

  form: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(4)]],
  });

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      if (params['sessionExpired']) {
        this.snackBar.open('Su sesión ha expirado. Por favor, inicie sesión nuevamente.', 'Cerrar', { duration: 5000 });
      }
      if (params['serverUnavailable']) {
        this.snackBar.open('El servidor no está disponible. Intente más tarde.', 'Cerrar', { duration: 5000 });
      }
    });
  }

  login(): void {
    if (this.form.invalid) return;
    this.cargando = true;

    this.authService.login(this.form.value).subscribe({
      next: () => {
        this.snackBar.open('Bienvenido', 'Cerrar', { duration: 2000 });
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.cargando = false;
        if (err.status === 0 || err.status === 503 || err.status === 504) {
          this.snackBar.open('El servidor no está disponible. Verifique la conexión.', 'Cerrar', { duration: 3000 });
        } else {
          this.snackBar.open(err.error?.error || 'Credenciales incorrectas', 'Cerrar', { duration: 3000 });
        }
        setTimeout(() => window.location.reload(), 1000);
      },
    });
  }
}
