export interface DatosRegistroCategoria {
  nombre: string;
  partida: string;
}

export interface DatosDetalleCategoria {
  id: number;
  nombre: string;
  partida: string;
}

export interface DatosActualizarCategoria {
  nombre?: string;
  partida?: string;
}
