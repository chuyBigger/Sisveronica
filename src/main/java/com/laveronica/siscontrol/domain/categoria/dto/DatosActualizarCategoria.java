package com.laveronica.siscontrol.domain.categoria.dto;

import com.laveronica.siscontrol.enums.Partida;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CategoriaActualizar")
public record DatosActualizarCategoria(

        String nombre,
        @Schema(description = "Partida presupuestal")
        Partida partida
) {
}
