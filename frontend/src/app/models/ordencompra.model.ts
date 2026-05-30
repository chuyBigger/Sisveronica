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
  cliente_id: number;
  contrato_id: number;
  partida: string;
  fechaInicioSemana: string;
  detalles: DatosRegistroOrdenCompraDetalle[];
}

export interface DatosListarOrdenCompra {
  id: number;
  cliente: string;
  contrato: string;
  partida: string;
  fechaInicioSemana: string;
  detalles: DatosListarDetalleOrdenCompraDetalle[];
}

export interface DatosDetalleOrdenCompra {
  id: number;
  cliente: string;
  contrato: string;
  partida: string;
  fechaInicioSemana: string;
  detalles: DatosDetalleOrdenCompraDetalle[];
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
  clienteId?: number;
  contrato_id?: number;
  partida?: string;
  fechaInicioSemana?: string;
  detalles?: DatosActualizarOrdenCompraDetalle[];
}
