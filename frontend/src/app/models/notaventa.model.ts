export interface NotaVentaDetalleRegistro {
  cantidad: number;
  productoId: number;
}

export interface NotaVentaListarDetalle {
  cantidad: number;
  producto: string;
  precio: number;
  subTotal: number;
}

export interface DatosRegistroNota {
  clienteId: number;
  partida: string;
  detalles: NotaVentaDetalleRegistro[];
}

export interface DatosListarNota {
  id: number;
  fecha: string;
  cliente: string;
  partida: string;
  detalles: NotaVentaListarDetalle[];
  totalGeneral: number;
}

export interface DatosDetalleNota {
  id: number;
  fecha: string;
  cliente: string;
  partida: string;
  detalles: NotaVentaListarDetalle[];
  totalGeneral: number;
}

export interface NotaVentaActualizarDetalle {
  cantidad: number;
  producto: string;
}

export interface DatosActualizarNota {
  partida: string;
  detalles: NotaVentaActualizarDetalle[];
}
