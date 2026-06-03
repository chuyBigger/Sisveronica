package com.laveronica.siscontrol.domain.productos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "ProductoActualizar")
public record DatosActualizarProducto(


        String nombre,

        @Schema(description = "Partida presupuestal")
        String partida,

        @Schema(description = "Identificador único de la categoría")
        String categoriaId,

        @Schema(description = "Unidad de medida del producto")
        String unidadMedida,

        @Schema(description = "Precio de compra")
        BigDecimal precioCompra,

        @Schema(description = "Precio de venta")
        BigDecimal precioVenta,

        String codigo

) {
}
