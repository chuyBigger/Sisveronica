package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosDetalleOrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "OrdenCompraDetalle")
public record DatosDetalleOrdenCompra(
        String id,
        String cliente,
        @Schema(description = "Nombre del contrato")
        String contrato,
        @Schema(description = "Partida presupuestal")
        Partida partida,
        @Schema(description = "Fecha de inicio de la semana")
        LocalDate fechaInicioSemana,
        List<DatosDetalleOrdenCompraDetalle> detalles,
        @Schema(description = "Usuario que confirmó")
        String confirmadoPor,
        @Schema(description = "Fecha de confirmación")
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
