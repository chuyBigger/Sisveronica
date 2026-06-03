package com.laveronica.siscontrol.domain.notacancelaciondetalle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(name = "CancelacionDetalleRegistro")
public record DatosRegistroCancelacionDetalle(
        @NotBlank @Schema(description = "Identificador único del producto") String productoId,
        @Positive @Schema(description = "Cantidad cancelada") Double cantidadCancelada
) {}
