package com.laveronica.siscontrol.domain.notacancelacion.dto;

import com.laveronica.siscontrol.domain.notacancelaciondetalle.dto.DatosRegistroCancelacionDetalle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(name = "CancelacionRegistro")
public record DatosRegistroCancelacion(
        @NotBlank @Schema(description = "Identificador único de la orden de compra") String ordenCompraId,
        @NotBlank @Schema(description = "Día de la semana") String dia,
        @NotEmpty List<DatosRegistroCancelacionDetalle> detalles
) {}
