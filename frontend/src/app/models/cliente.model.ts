export interface DatosRegistroCliente {
  nombre: string;
  rfc: string;
  calle?: string;
  numero?: number;
  fraccionamiento?: string;
  cp: string;
  municipio?: string;
  estado?: string;
}

export interface DatosDetalleCliente {
  id: number;
  nombre: string;
  rfc: string;
  calle: string;
  numero: number | null;
  fraccionamiento: string;
  cp: string;
  municipio: string;
  estado: string;
}

export interface DatosActualizarCliente {
  nombre?: string;
  calle?: string;
  numero?: number;
  fraccionamiento?: string;
  cp?: string;
  municipio?: string;
  estado?: string;
}
