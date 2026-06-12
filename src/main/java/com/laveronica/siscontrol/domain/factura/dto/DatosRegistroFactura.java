package com.laveronica.siscontrol.domain.factura.dto;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroFactura(
    @NotBlank String ordenCompraId
) {}
