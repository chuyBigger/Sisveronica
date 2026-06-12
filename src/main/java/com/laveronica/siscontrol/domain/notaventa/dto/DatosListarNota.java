package com.laveronica.siscontrol.domain.notaventa.dto;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaListarDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "NotaVentaListar")
public record DatosListarNota(
        String id,
        @Schema(description = "Número de folio")
        Integer folio,
        LocalDateTime fecha,
        String cliente,
        @Schema(description = "Partida presupuestal")
        String partida,
        List<NotaVentaListarDetalle> detalles,
        @Schema(description = "Total general")
        BigDecimal totalGeneral,
        @Schema(description = "Día de la semana")
        String dia,
        @Schema(description = "Está firmada")
        Boolean firmada,
        @Schema(description = "Detalle de incidencia")
        String detalle

) {
    public DatosListarNota(NotaVenta datos) {
        this(
                datos.getId(),
                datos.getFolio(),
                datos.getFecha(),
                datos.getCliente().getNombre(),
                datos.getPartida().name(),
                datos.getDetalles().stream()
                        .map(
                        detalle -> new NotaVentaListarDetalle(detalle))
                        .collect(Collectors.toList()),
                datos.getTotalGeneral(),
                datos.getDia(),
                datos.getFirmada(),
                datos.getDetalle()
        );
    }

}
