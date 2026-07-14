package com.laveronica.siscontrol.domain.extradetalle.dto;

import com.laveronica.siscontrol.domain.extradetalle.ExtraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Schema(name = "ExtraDetalleListar")
public record DatosListarExtraDetalle(
        String id,
        String producto,
        String productoNombre,
        Double cantidad,
        BigDecimal precio,
        BigDecimal subTotal
) {
    public DatosListarExtraDetalle(ExtraDetalle d) {
        this(
                d.getId(),
                d.getProducto().getId(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioVenta(),
                d.getPrecioVenta().multiply(BigDecimal.valueOf(d.getCantidad())).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
