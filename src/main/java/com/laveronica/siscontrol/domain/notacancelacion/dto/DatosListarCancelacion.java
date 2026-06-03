package com.laveronica.siscontrol.domain.notacancelacion.dto;

import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import com.laveronica.siscontrol.domain.notacancelaciondetalle.dto.DatosListarCancelacionDetalle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "CancelacionListar")
public record DatosListarCancelacion(
        String id,
        @Schema(description = "Identificador único de la orden de compra")
        String ordenCompraId,
        @Schema(description = "Día de la semana")
        String dia,
        @Schema(description = "Fecha de creación")
        LocalDateTime fechaCreacion,
        @Schema(description = "Usuario que creó el registro")
        String creadoPor,
        @Schema(description = "Usuario que validó")
        String validadoPor,
        @Schema(description = "Fecha de validación")
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
