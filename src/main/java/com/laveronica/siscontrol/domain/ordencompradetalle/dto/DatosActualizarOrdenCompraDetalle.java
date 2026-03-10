package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DatosActualizarOrdenCompraDetalle(

        @JsonProperty("producto")
        Long productoId,
        Double lunes,
        Double martes,
        Double miercoles,
        Double jueves,
        Double viernes,
        Double sabado,
        Double domingo

) {
}
