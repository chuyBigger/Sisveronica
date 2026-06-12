package com.laveronica.siscontrol.domain.productos.dto;

import com.laveronica.siscontrol.enums.UnidadMedida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(name = "ProductoRegistro")
public record DatosRegistroProducto(

        @NotNull(message = "El nombre del producto es obligatorio")
        String nombre,

        @NotBlank(message = "La partida es obligatoria")
        @Schema(description = "Partida presupuestal")
        String partida,

        @NotNull(message = "La categoría es obligatoria")
        @Schema(description = "Identificador único de la categoría")
        String categoriaId,

        @NotNull(message = "La unidad de medida es obligatoria")
        @Schema(description = "Unidad de medida del producto")
        UnidadMedida unidadMedida,

        @Schema(description = "Precio de compra")
        BigDecimal precioCompra,

        @NotNull(message = "El precio de venta es obligatorio")
        @Positive(message = "El precio de venta debe ser mayor a 0")
        @Schema(description = "Precio de venta")
        BigDecimal precioVenta,

        String codigo
) {



}

