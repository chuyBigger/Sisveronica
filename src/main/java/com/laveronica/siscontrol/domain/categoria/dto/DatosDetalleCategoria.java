package com.laveronica.siscontrol.domain.categoria.dto;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.enums.Partida;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CategoriaDetalle")
public record DatosDetalleCategoria(

        String id,
        String nombre,
        @Schema(description = "Partida presupuestal")
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
