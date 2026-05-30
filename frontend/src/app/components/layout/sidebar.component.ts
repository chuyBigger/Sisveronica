import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatListModule,
    MatIconModule,
    MatToolbarModule,
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent {
  menuItems = [
    { path: '/', icon: 'dashboard', label: 'Dashboard' },
    { path: '/productos', icon: 'inventory_2', label: 'Productos' },
    { path: '/categorias', icon: 'category', label: 'Categorías' },
    { path: '/clientes', icon: 'people', label: 'Clientes' },
    { path: '/contratos', icon: 'description', label: 'Contratos' },
    { path: '/notaventas', icon: 'receipt', label: 'Notas de Venta' },
    { path: '/ordenes-compra', icon: 'shopping_cart', label: 'Órdenes de Compra' },
  ];
}
