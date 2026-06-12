package com.laveronica.siscontrol.domain.reporte.dto;

import java.util.List;

public record DatosDiaReporte(
    String dia,
    String fecha,
    List<DatosProductoReporte> productos,
    double totalDia
) {}
