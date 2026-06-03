package com.laveronica.siscontrol.domain.notaventa.dto;

import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaActualizarDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "NotaVentaActualizar")
public record DatosActualizarNota(

        @Schema(description = "Partida presupuestal")
        String partida,
        List<NotaVentaActualizarDetalle> detalles

) {
}
