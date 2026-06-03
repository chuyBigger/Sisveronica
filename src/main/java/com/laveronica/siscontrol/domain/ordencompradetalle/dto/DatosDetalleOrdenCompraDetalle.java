package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "OrdenCompraDetalleInfo")
public record DatosDetalleOrdenCompraDetalle(

        String id,
        @Schema(description = "Nombre del producto")
        String producto,
        @Schema(description = "Nombre del producto")
        String productoNombre,
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
                detalle.getProducto().getNombre(),
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
