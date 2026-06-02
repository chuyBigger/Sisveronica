package com.laveronica.siscontrol.domain.notacancelaciondetalle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DatosRegistroCancelacionDetalle(
        @NotBlank String productoId,
        @Positive Double cantidadCancelada
) {}
