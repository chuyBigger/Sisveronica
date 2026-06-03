package com.laveronica.siscontrol.domain.notaventadetalle.dto;

import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "NotaVentaDetalleListar")
public record NotaVentaListarDetalle(
        Integer cantidad,
        @Schema(description = "Nombre del producto")
        String producto,
        BigDecimal precio,
        @Schema(description = "Subtotal")
        BigDecimal subTotal
) {
    public NotaVentaListarDetalle(NotaVentaDetalle detalles){
        this(
                detalles.getCantidad(),
                detalles.getProducto().getNombre(),
                detalles.getPrecioVenta(),
                detalles.getSubTotal()
        );


    }

}
