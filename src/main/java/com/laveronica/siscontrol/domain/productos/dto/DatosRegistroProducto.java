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

        String codigo,

        @Schema(description = "Clave del producto/servicio SAT")
        String claveProductoServicio,

        @Schema(description = "Clave de unidad de medida SAT")
        String claveUnidadMedida,

        @Schema(description = "Impuesto")
        BigDecimal impuesto,

        @Schema(description = "Descuentos")
        BigDecimal descuentos,

        @Schema(description = "IEPS 1")
        BigDecimal ieps1,

        @Schema(description = "IEPS 2")
        BigDecimal ieps2,

        @Schema(description = "Tipo de retención 1")
        String retencion1Tipo,

        @Schema(description = "Retención 1")
        BigDecimal retencion1,

        @Schema(description = "Tipo de retención 2")
        String retencion2Tipo,

        @Schema(description = "Retención 2")
        BigDecimal retencion2,

        @Schema(description = "Tipo de retención 3")
        String retencion3Tipo,

        @Schema(description = "Retención 3")
        BigDecimal retencion3,

        @Schema(description = "ID externo del sistema de facturación")
        Long idExterno
) {

}

