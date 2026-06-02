package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosDetalleOrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DatosDetalleOrdenCompra(
        String id,
        String cliente,
        String contrato,
        Partida partida,
        LocalDate fechaInicioSemana,
        List<DatosDetalleOrdenCompraDetalle> detalles,
        String confirmadoPor,
        LocalDateTime fechaConfirmacion

) {
    public DatosDetalleOrdenCompra(OrdenCompra ordenCompraNueva) {
        this(
                ordenCompraNueva.getId(),
                ordenCompraNueva.getCliente().getNombre(),
                ordenCompraNueva.getContrato().getContrato(),
                ordenCompraNueva.getPartida(),
                ordenCompraNueva.getFechaInicioSemana(),
                ordenCompraNueva.getDetalles().stream().map(
                        dn -> new DatosDetalleOrdenCompraDetalle(dn)
                ).collect(Collectors.toList()),
                ordenCompraNueva.getConfirmadoPor(),
                ordenCompraNueva.getFechaConfirmacion()
        );
    }
}
