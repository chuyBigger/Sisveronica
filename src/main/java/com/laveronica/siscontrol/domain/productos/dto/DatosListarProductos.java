package com.laveronica.siscontrol.domain.productos.dto;


import com.laveronica.siscontrol.domain.productos.Producto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ProductoListar")
public record DatosListarProductos(
        String id,
        String nombre,
        @Schema(description = "Partida presupuestal")
        String partida,
        String categoria,
        String codigo,
        @Schema(description = "Precio de venta")
        BigDecimal precioVenta,
        @Schema(description = "Unidad de medida")
        String unidadMedida,
        @Schema(description = "Clave del producto/servicio SAT")
        String claveProductoServicio,
        @Schema(description = "Clave de unidad de medida SAT")
        String claveUnidadMedida

) {

    public DatosListarProductos(Producto producto){

        this(
                producto.getId(),
                producto.getNombre(),
                producto.getPartida().name(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : "",
                producto.getCodigo(),
                producto.getPrecioVenta(),
                producto.getUnidadMedida().name(),
                producto.getClaveProductoServicio(),
                producto.getClaveUnidadMedida()
        );

    }
}
