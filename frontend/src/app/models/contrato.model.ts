export interface DatosRegistroContrato {
  contrato: string;
  clienteId: string;
  fechaInicio: string;
  fechaTermino: string;
  presupuesto: number;
  spei?: string;
  detalles?: string;
}

export interface DatosDetalleContrato {
  id: string;
  contrato: string;
  cliente: string;
  fechaInicio: string;
  fechaTermino: string;
  presupuesto: number;
  spei?: string;
  detalles?: string;
}

export interface DatosActualizarContrato {
  clienteId?: string;
  fechaInicio?: string;
  fechaTermino?: string;
  presupuesto?: number;
  spei?: string;
  detalles?: string;
}
