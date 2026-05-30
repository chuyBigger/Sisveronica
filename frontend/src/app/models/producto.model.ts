export interface DatosRegistroProducto {
  nombre: string;
  partida: string;
  categoriaId: number;
  unidadMedida: string;
  codigo?: string;
  precioCompra: number | null;
  precioVenta: number;
}

export interface DatosListarProductos {
  id: string;
  nombre: string;
  partida: string;
  categoria: string;
  codigo: string;
  precioVenta: number;
}

export interface DatosDetalleProducto {
  id: string;
  nombre: string;
  partida: string;
  categoriaId: number;
  unidadMedida: string;
  codigo: string;
  precioCompra: number | null;
  precioVenta: number;
}

export interface DatosActualizarProducto {
  nombre?: string;
  partida?: string;
  categoriaId?: number;
  unidadMedida?: string;
  codigo?: string;
  precioCompra?: number;
  precioVenta?: number;
}
