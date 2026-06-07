export interface DatosRegistroOrdenCompraDetalle {
  fecha: string;
  producto: string;
  lunes?: number;
  martes?: number;
  miercoles?: number;
  jueves?: number;
  viernes?: number;
  sabado?: number;
  domingo?: number;
}

export interface DatosDetalleOrdenCompraDetalle {
  fecha: string;
  producto: string;
  productoNombre: string;
  lunes: number;
  martes: number;
  miercoles: number;
  jueves: number;
  viernes: number;
  sabado: number;
  domingo: number;
}

export interface DatosListarDetalleOrdenCompraDetalle {
  fecha: string;
  producto: string;
  lunes: number;
  martes: number;
  miercoles: number;
  jueves: number;
  viernes: number;
  sabado: number;
  domingo: number;
}

export interface DatosRegistroOrdenCompra {
  cliente_id: string;
  contrato_id: string;
  partida: string;
  fechaInicioSemana: string;
  detalles: DatosRegistroOrdenCompraDetalle[];
}

export interface DatosListarOrdenCompra {
  id: string;
  cliente: string;
  contrato: string;
  partida: string;
  fechaInicioSemana: string;
  detalles: DatosListarDetalleOrdenCompraDetalle[];
  confirmadoPor?: string;
  fechaConfirmacion?: string;
  tieneFactura?: boolean;
  estado?: string;
  totalNotas?: number;
  notasFirmadas?: number;
  totalCancelaciones?: number;
  cancelacionesValidadas?: number;
}

export interface DatosDetalleOrdenCompra {
  id: string;
  cliente: string;
  contrato: string;
  partida: string;
  fechaInicioSemana: string;
  detalles: DatosDetalleOrdenCompraDetalle[];
  confirmadoPor?: string;
  fechaConfirmacion?: string;
}

export interface DatosActualizarOrdenCompraDetalle {
  fecha: string;
  producto: string;
  lunes?: number;
  martes?: number;
  miercoles?: number;
  jueves?: number;
  viernes?: number;
  sabado?: number;
  domingo?: number;
}

export interface DatosActulizarOrdenCompra {
  clienteId?: string;
  contrato_id?: string;
  partida?: string;
  fechaInicioSemana?: string;
  detalles?: DatosActualizarOrdenCompraDetalle[];
}
