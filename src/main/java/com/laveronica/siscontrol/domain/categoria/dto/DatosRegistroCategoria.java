package com.laveronica.siscontrol.domain.categoria.dto;

import com.laveronica.siscontrol.enums.Partida;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroCategoria(

        @NotNull
        String nombre,
        @NotNull
        Partida partida

) {
}
