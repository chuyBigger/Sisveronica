package com.laveronica.siscontrol.domain.notaventadetalle.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "NotaVentaDetalleRegistro")
public record NotaVentaDetalleRegistro(

        Integer cantidad,
        @Schema(description = "Identificador único del producto")
        String productoId

) {
}
