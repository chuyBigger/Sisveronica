package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosListarDetalleOrdenCompraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "OrdenCompraListar")
public record DatosListarOrdenCompra(
        String id,
        String cliente,
        @Schema(description = "Nombre del contrato")
        String contrato,
        @Schema(description = "Partida presupuestal")
        String partida,
        @Schema(description = "Fecha de inicio de la semana")
        LocalDate fechaInicioSemana,
        List<DatosListarDetalleOrdenCompraDetalle> detalles
) {
    public DatosListarOrdenCompra(OrdenCompra datos){
        this(
                datos.getId(),
                datos.getCliente().getNombre(),
                datos.getContrato().getContrato(),
                datos.getPartida().name(),
                datos.getFechaInicioSemana(),
                datos.getDetalles().stream()
                        .map(
                                ocd -> new DatosListarDetalleOrdenCompraDetalle(ocd)
                        )
                        .collect(Collectors.toList())
        );
    }
}


