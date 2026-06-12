package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrdenCompraDetalleActualizar")
public record DatosActualizarOrdenCompraDetalle(

        @JsonProperty("producto")
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
}
