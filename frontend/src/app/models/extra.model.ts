export interface ExtraDetalleRegistro {
  productoId: string;
  cantidad: number;
}

export interface ExtraListarDetalle {
  id: string;
  producto: string;
  productoNombre: string;
  cantidad: number;
  precio: number;
  subTotal: number;
}

export interface DatosRegistroExtra {
  ordenCompraId: string;
  dia: string;
  detalles: ExtraDetalleRegistro[];
}

export interface DatosListarExtra {
  id: string;
  ordenCompraId: string;
  dia: string;
  fecha: string;
  folio: number;
  firmada: boolean;
  fechaCreacion: string;
  creadoPor: string;
  detalles: ExtraListarDetalle[];
  totalGeneral: number;
}
