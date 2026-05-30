package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;

public record DatosListarDetalleOrdenCompraDetalle(

        String id,
        String productoId,
        Double lunes,
        Double martes,
        Double miercoles,
        Double jueves,
        Double viernes,
        Double sabado,
        Double domingo
) {
    public DatosListarDetalleOrdenCompraDetalle(OrdenCompraDetalle datos) {
        this(
                datos.getId(),
                datos.getProducto().getId().toString(),
                datos.getLunes(),
                datos.getMartes(),
                datos.getMiercoles(),
                datos.getJueves(),
                datos.getViernes(),
                datos.getSabado(),
                datos.getDomingo()
        );
    }
}
