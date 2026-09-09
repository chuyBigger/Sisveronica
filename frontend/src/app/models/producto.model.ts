export interface DatosRegistroProducto {
  nombre: string;
  partida: string;
  categoriaId: string;
  unidadMedida: string;
  codigo?: string;
  precioCompra: number | null;
  precioVenta: number;
  claveProductoServicio?: string;
  claveUnidadMedida?: string;
  impuesto?: number | null;
  descuentos?: number | null;
  ieps1?: number | null;
  ieps2?: number | null;
  retencion1Tipo?: string;
  retencion1?: number | null;
  retencion2Tipo?: string;
  retencion2?: number | null;
  retencion3Tipo?: string;
  retencion3?: number | null;
  idExterno?: number | null;
}

export interface DatosListarProductos {
  id: string;
  nombre: string;
  partida: string;
  categoria: string;
  codigo: string;
  precioVenta: number;
  unidadMedida?: string;
  claveProductoServicio?: string;
  claveUnidadMedida?: string;
}

export interface DatosDetalleProducto {
  id: string;
  nombre: string;
  partida: string;
  categoriaId: string;
  unidadMedida: string;
  codigo: string;
  precioCompra: number | null;
  precioVenta: number;
  claveProductoServicio?: string;
  claveUnidadMedida?: string;
  impuesto?: number | null;
  descuentos?: number | null;
  ieps1?: number | null;
  ieps2?: number | null;
  retencion1Tipo?: string;
  retencion1?: number | null;
  retencion2Tipo?: string;
  retencion2?: number | null;
  retencion3Tipo?: string;
  retencion3?: number | null;
  idExterno?: number | null;
}

export interface DatosActualizarProducto {
  nombre?: string;
  partida?: string;
  categoriaId?: string;
  unidadMedida?: string;
  codigo?: string;
  precioCompra?: number;
  precioVenta?: number;
  claveProductoServicio?: string;
  claveUnidadMedida?: string;
  impuesto?: number;
  descuentos?: number;
  ieps1?: number;
  ieps2?: number;
  retencion1Tipo?: string;
  retencion1?: number;
  retencion2Tipo?: string;
  retencion2?: number;
  retencion3Tipo?: string;
  retencion3?: number;
  idExterno?: number;
}
