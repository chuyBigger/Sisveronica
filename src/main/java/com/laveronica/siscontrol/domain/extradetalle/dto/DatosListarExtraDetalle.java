package com.laveronica.siscontrol.domain.extradetalle.dto;

import com.laveronica.siscontrol.domain.extradetalle.ExtraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ExtraDetalleListar")
public record DatosListarExtraDetalle(
        String id,
        String producto,
        String productoNombre,
        Double cantidad
) {
    public DatosListarExtraDetalle(ExtraDetalle d) {
        this(d.getId(), d.getProducto().getId(), d.getProducto().getNombre(), d.getCantidad());
    }
}
