package com.laveronica.siscontrol.domain.productos.dto;

import com.laveronica.siscontrol.domain.productos.Producto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


@Schema(name = "ProductoDetalle")
public record DatosDetalleProducto(

        @NotNull
        String id,

        @NotNull
        String nombre,

        @NotNull
        @Schema(description = "Partida presupuestal")
        String partida,

        @NotNull
        @Schema(description = "Identificador único de la categoría")
        String categoriaId,

        @NotNull
        @Schema(description = "Unidad de medida del producto")
        String unidadMedida,

        @Schema(description = "Precio de compra")
        BigDecimal precioCompra,

        @NotNull
        @Schema(description = "Precio de venta")
        BigDecimal precioVenta,

        String codigo

) {

    public DatosDetalleProducto(Producto producto){
        this(
                producto.getId(),
                producto.getNombre(),
                producto.getPartida().name(),
                producto.getCategoria() != null ? producto.getCategoria().getId() : "",
                producto.getUnidadMedida().name(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta(),
                producto.getCodigo()
        );
    }


}
