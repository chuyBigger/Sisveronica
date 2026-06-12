package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "OrdenCompraDetalleRegistro")
public record DatosRegistroOrdenCompraDetalle(

        LocalDate fecha,
        @Schema(description = "Nombre del producto")
        String producto,
        Double lunes,
        Double martes,
        Double miercoles,
        Double jueves,
        Double viernes,
        Double sabado,
        Double domingo
) {
}
