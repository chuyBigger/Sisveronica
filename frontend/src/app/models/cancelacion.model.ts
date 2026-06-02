export interface CancelacionDetalleRegistro {
  productoId: string;
  cantidadCancelada: number;
}

export interface CancelacionListarDetalle {
  id: string;
  producto: string;
  productoNombre: string;
  cantidadCancelada: number;
}

export interface DatosRegistroCancelacion {
  ordenCompraId: string;
  dia: string;
  detalles: CancelacionDetalleRegistro[];
}

export interface DatosListarCancelacion {
  id: string;
  ordenCompraId: string;
  dia: string;
  fechaCreacion: string;
  creadoPor: string;
  validadoPor?: string;
  fechaValidacion?: string;
  detalles: CancelacionListarDetalle[];
}
