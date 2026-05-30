package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosListarDetalleOrdenCompraDetalle;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record DatosListarOrdenCompra(
        String id,
        String cliente,
        String contrato,
        String partida,
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


