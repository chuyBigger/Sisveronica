package com.laveronica.siscontrol.domain.notacancelaciondetalle.dto;

import com.laveronica.siscontrol.domain.notacancelaciondetalle.NotaCancelacionDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CancelacionDetalleListar")
public record DatosListarCancelacionDetalle(
        String id,
        @Schema(description = "Nombre del producto")
        String producto,
        @Schema(description = "Nombre del producto")
        String productoNombre,
        @Schema(description = "Cantidad cancelada")
        Double cantidadCancelada
) {
    public DatosListarCancelacionDetalle(NotaCancelacionDetalle d) {
        this(d.getId(), d.getProducto().getId().toString(), d.getProducto().getNombre(), d.getCantidadCancelada());
    }
}
