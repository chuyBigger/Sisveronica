package com.laveronica.siscontrol.domain.reporte.dto;

import java.util.List;

public record DatosReporteProduccionCarne(
    String semanaInicio,
    String semanaFin,
    List<DatosClienteReporte> clientes
) {}
