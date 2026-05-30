package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;

import java.time.LocalDate;

public record DatosDetalleOrdenCompraDetalle(

        String id,
        String producto,
        Double lunes,
        Double martes,
        Double miercoles,
        Double jueves,
        Double viernes,
        Double sabado,
        Double domingo

) {
    public DatosDetalleOrdenCompraDetalle(OrdenCompraDetalle detalle) {
        this(
                detalle.getId(),
                detalle.getProducto().getId().toString(),
                detalle.getLunes(),
                detalle.getMartes(),
                detalle.getMiercoles(),
                detalle.getJueves(),
                detalle.getViernes(),
                detalle.getSabado(),
                detalle.getDomingo()
        );
    }
}
