package com.laveronica.siscontrol.domain.categoria.dto;

import com.laveronica.siscontrol.enums.Partida;

public record DatosActualizarCategoria(

        String nombre,
        Partida partida
) {
}
