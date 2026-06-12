package com.laveronica.siscontrol.domain.extradetalle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(name = "ExtraDetalleRegistro")
public record DatosRegistroExtraDetalle(
        @NotBlank String productoId,
        @Positive Double cantidad
) {}
