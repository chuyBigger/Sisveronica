package com.laveronica.siscontrol.domain.categoria.dto;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.enums.Partida;

public record DatosDetalleCategoria(

        Long id,
        String nombre,
        Partida partida
) {
    public DatosDetalleCategoria( Categoria categoria){
        this(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getPartida()
        );
    }
}
