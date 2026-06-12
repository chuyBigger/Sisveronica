import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ProductoService } from '../../../services/producto.service';
import { ClienteService } from '../../../services/cliente.service';
import { ContratoService } from '../../../services/contrato.service';
import { NotaVentaService } from '../../../services/notaventa.service';
import { OrdenCompraService } from '../../../services/ordencompra.service';
import { forkJoin } from 'rxjs';

interface DashboardCard {
  title: string;
  count: number;
  icon: string;
  route: string;
  color: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatGridListModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private productoService = inject(ProductoService);
  private clienteService = inject(ClienteService);
  private contratoService = inject(ContratoService);
  private notaventaService = inject(NotaVentaService);
  private ordenService = inject(OrdenCompraService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  cards: DashboardCard[] = [
    { title: 'Productos', count: 0, icon: 'inventory_2', route: '/productos', color: '#1976d2' },
    { title: 'Clientes', count: 0, icon: 'people', route: '/clientes', color: '#f57c00' },
    { title: 'Contratos', count: 0, icon: 'description', route: '/contratos', color: '#7b1fa2' },
    { title: 'Notas de Venta', count: 0, icon: 'receipt', route: '/notaventas', color: '#c62828' },
    { title: 'Órdenes de Compra', count: 0, icon: 'shopping_cart', route: '/ordenes-compra', color: '#00838f' },
  ];

  ngOnInit(): void {
    this.cargarConteos();
  }

  cargarConteos(): void {
    forkJoin({
      productos: this.productoService.listar(0, 1),
      clientes: this.clienteService.listar(),
      contratos: this.contratoService.listar(),
      notas: this.notaventaService.listar(0, 1),
      ordenes: this.ordenService.listar(0, 1),
    }).subscribe({
      next: (res) => {
        this.cards[0].count = res.productos.totalElements ?? (res.productos.content ? res.productos.content.length : 0);
        this.cards[1].count = res.clientes.length;
        this.cards[2].count = res.contratos.length;
        this.cards[3].count = res.notas.totalElements ?? (res.notas.content ? res.notas.content.length : 0);
        this.cards[4].count = res.ordenes.totalElements ?? (res.ordenes.content ? res.ordenes.content.length : 0);
        this.cdr.detectChanges();
      },
      error: () => this.snackBar.open('Error al cargar datos del dashboard', 'Cerrar', { duration: 3000 }),
    });
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }
}
