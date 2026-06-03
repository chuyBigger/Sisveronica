package com.laveronica.siscontrol.domain.notaventa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "NotaVentaGenerar")
public record DatosGenerarNotaDesdeOrden(
        @NotBlank @Schema(description = "Identificador único de la orden de compra") String ordenCompraId,
        @NotBlank @Schema(description = "Día de la semana") String dia
) {
}
