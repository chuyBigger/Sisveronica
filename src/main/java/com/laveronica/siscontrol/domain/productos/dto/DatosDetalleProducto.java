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

    public DatosDetalleProducto(Producto producto){
        this(
                producto.getId(),
                producto.getNombre(),
                producto.getPartida().name(),
                producto.getCategoria() != null ? producto.getCategoria().getId() : "",
                producto.getUnidadMedida().name(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta(),
                producto.getCodigo(),
                producto.getClaveProductoServicio(),
                producto.getClaveUnidadMedida(),
                producto.getImpuesto(),
                producto.getDescuentos(),
                producto.getIeps1(),
                producto.getIeps2(),
                producto.getRetencion1Tipo(),
                producto.getRetencion1(),
                producto.getRetencion2Tipo(),
                producto.getRetencion2(),
                producto.getRetencion3Tipo(),
                producto.getRetencion3(),
                producto.getIdExterno()
        );
    }


}
