package com.laveronica.siscontrol.domain.productos.productomapper;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.productos.dto.DatosDetalleProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.enums.Partida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "categoriaId", source = "producto.categoria.id")
    @Mapping(target = "nombre", expression = "java(producto.getNombre().toLowerCase().trim())")
    DatosDetalleProducto toDetalleDto(Producto producto);

    @Mapping(target = "nombre", expression = "java(datos.nombre().trim().toLowerCase())")
    @Mapping(target = "partida", source = "partida")
    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", expression = "java(datos.codigo())")
    Producto toEntity(DatosRegistroProducto datos, Partida partida, Categoria categoria);

}
