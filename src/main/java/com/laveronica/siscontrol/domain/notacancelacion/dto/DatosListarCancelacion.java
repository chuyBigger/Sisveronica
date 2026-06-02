package com.laveronica.siscontrol.domain.notacancelacion.dto;

import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import com.laveronica.siscontrol.domain.notacancelaciondetalle.dto.DatosListarCancelacionDetalle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DatosListarCancelacion(
        String id,
        String ordenCompraId,
        String dia,
        LocalDateTime fechaCreacion,
        String creadoPor,
        String validadoPor,
        LocalDateTime fechaValidacion,
        List<DatosListarCancelacionDetalle> detalles
) {
    public DatosListarCancelacion(NotaCancelacion nc) {
        this(
                nc.getId(),
                nc.getOrdenCompra().getId(),
                nc.getDia(),
                nc.getFechaCreacion(),
                nc.getCreadoPor(),
                nc.getValidadoPor(),
                nc.getFechaValidacion(),
                nc.getDetalles().stream().map(DatosListarCancelacionDetalle::new).collect(Collectors.toList())
        );
    }
}
