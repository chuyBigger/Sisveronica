package com.laveronica.siscontrol.domain.notacancelacion.dto;

import com.laveronica.siscontrol.domain.notacancelaciondetalle.dto.DatosRegistroCancelacionDetalle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DatosRegistroCancelacion(
        @NotBlank String ordenCompraId,
        @NotBlank String dia,
        @NotEmpty List<DatosRegistroCancelacionDetalle> detalles
) {}
