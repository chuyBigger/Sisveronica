import { Routes } from '@angular/router';
import { AuthGuard, AdminGuard } from './services/auth.guard';
import { DashboardComponent } from './components/layout/dashboard/dashboard.component';
import { ProductoListaComponent } from './components/productos/producto-lista.component';
import { ProductoFormComponent } from './components/productos/producto-form.component';
import { ClienteListaComponent } from './components/clientes/cliente-lista.component';
import { ClienteFormComponent } from './components/clientes/cliente-form.component';
import { ContratoListaComponent } from './components/contratos/contrato-lista.component';
import { ContratoFormComponent } from './components/contratos/contrato-form.component';
import { NotaVentaListaComponent } from './components/notaventas/notaventa-lista.component';
import { NotaVentaFormComponent } from './components/notaventas/notaventa-form.component';
import { NotaVentaDetalleComponent } from './components/notaventas/notaventa-detalle.component';
import { OrdenListaComponent } from './components/ordenes-compra/orden-lista.component';
import { OrdenFormComponent } from './components/ordenes-compra/orden-form.component';
import { OrdenDetalleComponent } from './components/ordenes-compra/orden-detalle/orden-detalle.component';
import { LoginComponent } from './components/auth/login.component';
import { ConfigComponent } from './components/config/config.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'productos', component: ProductoListaComponent, canActivate: [AuthGuard] },
  { path: 'productos/nuevo', component: ProductoFormComponent, canActivate: [AuthGuard] },
  { path: 'productos/:id', component: ProductoFormComponent, canActivate: [AuthGuard] },
  { path: 'clientes', component: ClienteListaComponent, canActivate: [AuthGuard] },
  { path: 'clientes/nuevo', component: ClienteFormComponent, canActivate: [AuthGuard] },
  { path: 'clientes/:id', component: ClienteFormComponent, canActivate: [AuthGuard] },
  { path: 'contratos', component: ContratoListaComponent, canActivate: [AuthGuard] },
  { path: 'contratos/nuevo', component: ContratoFormComponent, canActivate: [AuthGuard] },
  { path: 'contratos/:id', component: ContratoFormComponent, canActivate: [AuthGuard] },
  { path: 'notaventas', component: NotaVentaListaComponent, canActivate: [AuthGuard] },
  { path: 'notaventas/nuevo', component: NotaVentaFormComponent, canActivate: [AuthGuard] },
  { path: 'notaventas/:id', component: NotaVentaFormComponent, canActivate: [AuthGuard] },
  { path: 'notaventas/:id/ver', component: NotaVentaDetalleComponent, canActivate: [AuthGuard] },
  { path: 'ordenes-compra', component: OrdenListaComponent, canActivate: [AuthGuard] },
  { path: 'ordenes-compra/nuevo', component: OrdenFormComponent, canActivate: [AuthGuard] },
  { path: 'ordenes-compra/:id/ver', component: OrdenDetalleComponent, canActivate: [AuthGuard] },
  { path: 'ordenes-compra/:id', component: OrdenFormComponent, canActivate: [AuthGuard] },
  { path: 'config', component: ConfigComponent, canActivate: [AuthGuard] },
];
