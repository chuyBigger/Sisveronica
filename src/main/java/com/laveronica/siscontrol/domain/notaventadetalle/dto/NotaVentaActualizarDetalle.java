package com.laveronica.siscontrol.domain.notaventadetalle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "NotaVentaDetalleActualizar")
public record NotaVentaActualizarDetalle(

        Integer cantidad,
        @Schema(description = "Nombre del producto")
        String producto

) {
}
