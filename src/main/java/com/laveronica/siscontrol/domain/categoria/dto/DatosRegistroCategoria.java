package com.laveronica.siscontrol.domain.categoria.dto;

import com.laveronica.siscontrol.enums.Partida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CategoriaRegistro")
public record DatosRegistroCategoria(

        @NotNull
        String nombre,
        @NotNull
        @Schema(description = "Partida presupuestal")
        Partida partida

) {
}
