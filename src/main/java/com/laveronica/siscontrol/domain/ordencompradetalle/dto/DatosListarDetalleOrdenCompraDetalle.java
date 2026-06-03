package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrdenCompraDetalleListar")
public record DatosListarDetalleOrdenCompraDetalle(

        String id,
        @Schema(description = "Identificador único del producto")
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
