package com.laveronica.siscontrol.domain.reporte.dto;

public record DatosProductoReporte(
    String productoNombre,
    double cantidad,
    String unidadMedida
) {}
