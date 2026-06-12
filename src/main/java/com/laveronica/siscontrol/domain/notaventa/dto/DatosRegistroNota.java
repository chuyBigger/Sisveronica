package com.laveronica.siscontrol.domain.notaventa.dto;

import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaDetalleRegistro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name = "NotaVentaRegistro")
public record DatosRegistroNota(

        @NotNull
        @Schema(description = "Identificador único del cliente")
        String clienteId,
        @Schema(description = "Partida presupuestal")
        String partida,
        List<NotaVentaDetalleRegistro> detalles


) {
}
