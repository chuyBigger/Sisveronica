import { Component, inject } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent {
  authService = inject(AuthService);
  private router = inject(Router);

  menuItems = [
    { path: '/dashboard', icon: 'dashboard', label: 'Dashboard' },
    { path: '/productos', icon: 'inventory_2', label: 'Productos' },
    { path: '/clientes', icon: 'people', label: 'Clientes' },
    { path: '/contratos', icon: 'description', label: 'Contratos' },
    { path: '/notaventas', icon: 'receipt', label: 'Notas de Venta' },
    { path: '/ordenes-compra', icon: 'shopping_cart', label: 'Órdenes de Compra' },
  ];

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
