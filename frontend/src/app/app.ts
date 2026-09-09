import { Component, OnInit, OnDestroy, inject, NgZone } from '@angular/core';
import { Router, RouterOutlet, RouterModule } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatTooltipModule,
    MatDividerModule,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit, OnDestroy {
  authService = inject(AuthService);
  router = inject(Router);
  private ngZone = inject(NgZone);

  get isLoginRoute(): boolean {
    return this.router.url === '/' || this.router.url.startsWith('/?');
  }

  fecha = new Date().toLocaleDateString('es-MX', {
    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
  });
  hora = new Date().toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  darkMode = false;
  sidebarOpen = true;
  private timerId: any;

  menuItems = [
    { path: '/dashboard', icon: 'dashboard', label: 'Dashboard' },
    { path: '/productos', icon: 'inventory_2', label: 'Productos' },
    { path: '/clientes', icon: 'people', label: 'Clientes' },
    { path: '/contratos', icon: 'description', label: 'Contratos' },
    { path: '/notaventas', icon: 'receipt', label: 'Notas de Venta' },
    { path: '/ordenes-compra', icon: 'shopping_cart', label: 'Órdenes de Compra' },
    { path: '/reportes/produccion', icon: 'assessment', label: 'Reporte Producción' },
  ];

  adminMenuItems = [
    { path: '/admin/super', icon: 'admin_panel_settings', label: 'Super Admin' },
  ];

  ngOnInit(): void {
    this.darkMode = localStorage.getItem('darkMode') === 'true';
    if (this.darkMode) document.body.classList.add('dark-mode');
    this.sidebarOpen = localStorage.getItem('sidebarOpen') !== 'false';
    this.ngZone.runOutsideAngular(() => {
      this.timerId = setInterval(() => {
        this.ngZone.run(() => this.actualizarReloj());
      }, 1000);
    });
  }

  ngOnDestroy(): void {
    clearInterval(this.timerId);
  }

  private actualizarReloj(): void {
    const now = new Date();
    this.fecha = now.toLocaleDateString('es-MX', {
      weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
    });
    this.hora = now.toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  }

  irASettings(): void {
    this.router.navigate(['/config']);
  }

  toggleDarkMode(): void {
    this.darkMode = !this.darkMode;
    document.body.classList.toggle('dark-mode', this.darkMode);
    localStorage.setItem('darkMode', String(this.darkMode));
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
    localStorage.setItem('sidebarOpen', String(this.sidebarOpen));
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
