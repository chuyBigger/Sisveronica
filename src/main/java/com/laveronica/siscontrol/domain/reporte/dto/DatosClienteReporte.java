package com.laveronica.siscontrol.domain.reporte.dto;

import java.util.List;

public record DatosClienteReporte(
    String clienteNombre,
    double totalGeneral,
    List<DatosDiaReporte> dias
) {}
