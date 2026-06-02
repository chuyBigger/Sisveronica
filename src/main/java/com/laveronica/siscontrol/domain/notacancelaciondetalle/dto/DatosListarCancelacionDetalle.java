package com.laveronica.siscontrol.domain.notacancelaciondetalle.dto;

import com.laveronica.siscontrol.domain.notacancelaciondetalle.NotaCancelacionDetalle;

public record DatosListarCancelacionDetalle(
        String id,
        String producto,
        String productoNombre,
        Double cantidadCancelada
) {
    public DatosListarCancelacionDetalle(NotaCancelacionDetalle d) {
        this(d.getId(), d.getProducto().getId().toString(), d.getProducto().getNombre(), d.getCantidadCancelada());
    }
}
