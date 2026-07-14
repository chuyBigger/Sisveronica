package com.laveronica.siscontrol.domain.extra.dto;

import com.laveronica.siscontrol.domain.extra.Extra;
import com.laveronica.siscontrol.domain.extradetalle.dto.DatosListarExtraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "ExtraListar")
public record DatosListarExtra(
        String id,
        String ordenCompraId,
        String dia,
        LocalDate fecha,
        Integer folio,
        Boolean firmada,
        LocalDateTime fechaCreacion,
        String creadoPor,
        List<DatosListarExtraDetalle> detalles,
        BigDecimal totalGeneral
) {
    public DatosListarExtra(Extra e) {
        this(
                e.getId(),
                e.getOrdenCompra().getId(),
                e.getDia(),
                e.getFecha(),
                e.getFolio(),
                e.getFirmada(),
                e.getFechaCreacion(),
                e.getCreadoPor(),
                e.getDetalles().stream().map(DatosListarExtraDetalle::new).collect(Collectors.toList()),
                e.getDetalles().stream()
                        .map(d -> d.getPrecioVenta().multiply(BigDecimal.valueOf(d.getCantidad())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}
