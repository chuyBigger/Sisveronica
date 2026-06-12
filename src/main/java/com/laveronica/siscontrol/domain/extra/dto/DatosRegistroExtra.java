package com.laveronica.siscontrol.domain.extra.dto;

import com.laveronica.siscontrol.domain.extradetalle.dto.DatosRegistroExtraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(name = "ExtraRegistro")
public record DatosRegistroExtra(
        @NotBlank String ordenCompraId,
        @NotBlank String dia,
        @NotEmpty List<DatosRegistroExtraDetalle> detalles
) {}
