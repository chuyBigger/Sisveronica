import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { OrdenCompraService } from '../../../services/ordencompra.service';
import { CancelacionService } from '../../../services/cancelacion.service';
import { DatosDetalleOrdenCompra } from '../../../models/ordencompra.model';
import { DatosListarCancelacion } from '../../../models/cancelacion.model';
import { NotaVentaPreviewDialogComponent } from '../../notaventas/notaventa-preview-dialog.component';
import { CancelacionFormDialogComponent, CancelacionFormData } from '../cancelacion-form-dialog.component';
import { FacturaService } from '../../../services/factura.service';
import { Factura } from '../../../models/factura.model';

@Component({
  selector: 'app-orden-detalle',
  standalone: true,
  imports: [
    CommonModule, RouterModule, MatButtonModule, MatIconModule,
    MatCardModule, MatDividerModule, MatSnackBarModule, MatDialogModule, MatTooltipModule,
  ],
  templateUrl: './orden-detalle.component.html',
  styleUrl: './orden-detalle.component.scss',
})
export class OrdenDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private ordenService = inject(OrdenCompraService);
  private cancelacionService = inject(CancelacionService);
  private facturaService = inject(FacturaService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);
  private cdr = inject(ChangeDetectorRef);

  orden!: DatosDetalleOrdenCompra;
  notas: any[] = [];
  cancelaciones: DatosListarCancelacion[] = [];
  factura: Factura | null = null;
  cargando = true;
  generando = false;
  generandoFactura = false;
  reconstruyendo = false;

  readonly dias = ['lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'];
  readonly diasCorto = ['Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá', 'Do'];
  readonly ordenDias = ['martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo', 'lunes'];

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarOrden(id);
      this.cargarNotas(id);
      this.cargarCancelaciones(id);
      this.cargarFactura(id);
    }
  }

  private cargarOrden(id: string): void {
    this.ordenService.buscarPorId(id).subscribe({
      next: (res) => {
        this.orden = res;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Error al cargar orden de compra', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/ordenes-compra']);
      },
    });
  }

  private cargarNotas(id: string): void {
    this.ordenService.listarNotasPorOrden(id).subscribe({
      next: (res) => {
        this.notas = (res ?? []).sort(
          (a: any, b: any) => this.ordenDias.indexOf(a.dia) - this.ordenDias.indexOf(b.dia)
        );
        this.cdr.detectChanges();
      },
    });
  }

  private cargarCancelaciones(id: string): void {
    this.cancelacionService.listarPorOrden(id).subscribe({
      next: (res) => {
        this.cancelaciones = res ?? [];
        this.cdr.detectChanges();
      },
    });
  }

  volver(): void {
    this.router.navigate(['/ordenes-compra']);
  }

  editar(): void {
    this.router.navigate(['/ordenes-compra', this.orden.id]);
  }

  eliminar(): void {
    if (confirm('¿Eliminar esta orden de compra?')) {
      this.ordenService.eliminar(this.orden.id).subscribe({
        next: () => {
          this.snackBar.open('Orden de compra eliminada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/ordenes-compra']);
        },
        error: () => this.snackBar.open('Error al eliminar orden', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  confirmar(): void {
    if (!this.orden || this.orden.confirmadoPor) return;
    if (confirm('¿Confirmar esta orden de compra? Esta acción no se puede deshacer.')) {
      this.ordenService.confirmar(this.orden.id).subscribe({
        next: (res) => {
          this.orden = res;
          this.snackBar.open('Orden confirmada por ' + res.confirmadoPor, 'Cerrar', { duration: 3000 });
          this.cdr.detectChanges();
        },
        error: () => this.snackBar.open('Error al confirmar orden', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  generarNotas(): void {
    if (this.generando) return;
    this.generando = true;
    this.ordenService.generarTodasNotas(this.orden.id).subscribe({
      next: (notas) => {
        this.snackBar.open(`Notas generadas: ${notas.length}`, 'Cerrar', { duration: 3000 });
        this.cargarNotas(this.orden.id);
        this.generando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Error al generar notas', 'Cerrar', { duration: 3000 });
        this.generando = false;
      },
    });
  }

  abrirNota(nota: any): void {
    this.dialog.open(NotaVentaPreviewDialogComponent, {
      data: nota,
      width: '900px',
      maxWidth: '95vw',
      maxHeight: '90vh',
      panelClass: 'notaventa-preview-dialog',
    });
  }

  abrirCrearCancelacion(): void {
    const dialogRef = this.dialog.open(CancelacionFormDialogComponent, {
      data: { orden: this.orden } as CancelacionFormData,
      width: '600px',
      maxWidth: '95vw',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'saved') this.cargarCancelaciones(this.orden.id);
    });
  }

  validarCancelacion(id: string): void {
    if (confirm('¿Validar esta cancelación? Esta acción no se puede deshacer.')) {
      this.cancelacionService.validar(id).subscribe({
        next: () => {
          this.snackBar.open('Cancelación validada — nota actualizada', 'Cerrar', { duration: 2000 });
          this.cargarCancelaciones(this.orden.id);
          this.cargarNotas(this.orden.id);
        },
        error: () => this.snackBar.open('Error al validar cancelación', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  eliminarCancelacion(id: string): void {
    if (confirm('¿Eliminar esta cancelación?')) {
      this.cancelacionService.eliminar(id).subscribe({
        next: () => {
          this.snackBar.open('Cancelación eliminada', 'Cerrar', { duration: 2000 });
          this.cargarCancelaciones(this.orden.id);
        },
        error: () => this.snackBar.open('Error al eliminar cancelación', 'Cerrar', { duration: 3000 }),
      });
    }
  }

  reconstruirNotas(): void {
    if (this.reconstruyendo) return;
    if (!confirm('¿Reconstruir notas afectadas por cancelaciones?')) return;
    this.reconstruyendo = true;
    this.cancelacionService.reconstruirNotas(this.orden.id).subscribe({
      next: (notas) => {
        this.snackBar.open(`Notas actualizadas: ${notas.length}`, 'Cerrar', { duration: 3000 });
        this.cargarNotas(this.orden.id);
        this.reconstruyendo = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Error al reconstruir notas', 'Cerrar', { duration: 3000 });
        this.reconstruyendo = false;
      },
    });
  }

  private cargarFactura(id: string): void {
    this.facturaService.obtenerPorOrdenCompraId(id).subscribe({
      next: (res) => {
        this.factura = res;
        this.cdr.detectChanges();
      },
      error: () => {
        this.factura = null;
        this.cdr.detectChanges();
      },
    });
  }

  get puedeGenerarFactura(): boolean {
    if (!this.orden?.confirmadoPor) return false;
    if (this.notas.length === 0) return false;
    const todasFirmadas = this.notas.every(n => n.firmada);
    const cancelacionesValidadas = this.cancelaciones.every(c => !!c.validadoPor);
    return todasFirmadas && cancelacionesValidadas;
  }

  generarFactura(): void {
    if (this.generandoFactura || !this.puedeGenerarFactura) return;
    if (!confirm('¿Generar factura/prefactura para esta orden de compra?')) return;
    this.generandoFactura = true;
    this.facturaService.generar({ ordenCompraId: this.orden.id }).subscribe({
      next: (res) => {
        this.factura = res;
        this.snackBar.open(`Factura #${res.folio} generada exitosamente`, 'Cerrar', { duration: 3000 });
        this.generandoFactura = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al generar factura';
        this.snackBar.open(msg, 'Cerrar', { duration: 5000 });
        this.generandoFactura = false;
      },
    });
  }

  notaTieneCancelacion(nota: any): boolean {
    if (!nota.dia) return false;
    return this.cancelaciones.some(c => c.dia === nota.dia && !!c.validadoPor);
  }

  getNotaClase(nota: any): string {
    if (nota.firmada) return 'nota-firmada';
    if (nota.detalle) return 'nota-vencida';
    const fechaNota = new Date(nota.fecha);
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    if (fechaNota < hoy) return 'nota-vencida';
    return '';
  }

  getValor(detalle: any, dia: string): string {
    const v = detalle[dia];
    return v != null ? v : '-';
  }

  getFinSemana(): Date {
    const inicio = new Date(this.orden.fechaInicioSemana);
    const fin = new Date(inicio);
    fin.setDate(fin.getDate() + 6);
    return fin;
  }

  sumarDia(detalles: any[], dia: string): number {
    return detalles.reduce((sum: number, d: any) => sum + (d[dia] || 0), 0);
  }

  totalRow(detalle: any): number {
    return this.dias.reduce((sum, d) => sum + (detalle[d] || 0), 0);
  }

  totalGeneral(): number {
    return this.orden.detalles.reduce((sum, d) => sum + this.totalRow(d), 0);
  }
}
