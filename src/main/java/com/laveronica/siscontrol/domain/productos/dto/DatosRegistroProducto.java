package com.laveronica.siscontrol.domain.productos.dto;

import com.laveronica.siscontrol.enums.UnidadMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DatosRegistroProducto(

        @NotNull(message = "El nombre del producto es obligatorio")
        String nombre,

        @NotBlank(message = "La partida es obligatoria")
        String partida,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @NotNull(message = "La unidad de medida es obligatoria")
        UnidadMedida unidadMedida,

        BigDecimal precioCompra,

        @NotNull(message = "El precio de venta es obligatorio")
        @Positive(message = "El precio de venta debe ser mayor a 0")
        BigDecimal precioVenta
) {
}

