export interface FacturaDetalle {
  id: string;
  productoNombre: string;
  cantidadTotal: number;
  precioVenta: number;
  subtotal: number;
}

export interface Factura {
  id: string;
  folio: number;
  ordenCompraId: string;
  cliente: string;
  contrato: string;
  partida: string;
  fechaCreacion: string;
  totalGeneral: number;
  detalles: FacturaDetalle[];
}

export interface DatosRegistroFactura {
  ordenCompraId: string;
}
