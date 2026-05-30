export interface DatosRegistroProducto {
  nombre: string;
  partida: string;
  categoriaId: number;
  unidadMedida: string;
  precioCompra: number | null;
  precioVenta: number;
}

export interface DatosListarProductos {
  id: number;
  nombre: string;
  partida: string;
  categoria: string;
  precioVenta: number;
}

export interface DatosDetalleProducto {
  id: number;
  nombre: string;
  partida: string;
  categoriaId: number;
  unidadMedida: string;
  precioCompra: number | null;
  precioVenta: number;
}

export interface DatosActualizarProducto {
  nombre?: string;
  partida?: string;
  categoriaId?: number;
  unidadMedida?: string;
  precioCompra?: number;
  precioVenta?: number;
}
