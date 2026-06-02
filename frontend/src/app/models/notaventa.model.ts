export interface NotaVentaDetalleRegistro {
  cantidad: number;
  productoId: string;
}

export interface NotaVentaListarDetalle {
  cantidad: number;
  producto: string;
  precio: number;
  subTotal: number;
}

export interface DatosRegistroNota {
  clienteId: string;
  partida: string;
  detalles: NotaVentaDetalleRegistro[];
}

export interface DatosListarNota {
  id: string;
  folio?: number;
  fecha: string;
  cliente: string;
  partida: string;
  detalles: NotaVentaListarDetalle[];
  totalGeneral: number;
  dia?: string;
}

export interface DatosDetalleNota {
  id: string;
  folio?: number;
  fecha: string;
  cliente: string;
  partida: string;
  detalles: NotaVentaListarDetalle[];
  totalGeneral: number;
  dia?: string;
}

export interface NotaVentaActualizarDetalle {
  cantidad: number;
  producto: string;
}

export interface DatosActualizarNota {
  partida: string;
  detalles: NotaVentaActualizarDetalle[];
}
