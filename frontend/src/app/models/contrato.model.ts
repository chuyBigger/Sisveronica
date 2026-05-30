export interface DatosRegistroContrato {
  contrato: string;
  clienteId: number;
  fechaInicio: string;
  fechaTermino: string;
  presupuesto: number;
}

export interface DatosDetalleContrato {
  id: number;
  contrato: string;
  cliente: string;
  fechaInicio: string;
  fechaTermino: string;
  presupuesto: number;
}

export interface DatosActualizarContrato {
  clienteId?: number;
  fechaInicio?: string;
  fechaTermino?: string;
  presupuesto?: number;
}
