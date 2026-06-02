package com.laveronica.siscontrol.domain.notaventa.dto;

import jakarta.validation.constraints.NotBlank;

public record DatosGenerarNotaDesdeOrden(
        @NotBlank String ordenCompraId,
        @NotBlank String dia
) {
}
