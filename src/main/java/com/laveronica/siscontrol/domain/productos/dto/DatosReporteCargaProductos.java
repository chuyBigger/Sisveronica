package com.laveronica.siscontrol.domain.productos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "ProductoReporteCarga")
public record DatosReporteCargaProductos(
        @Schema(description = "Total de registros procesados")
        int totalProcesados,
        @Schema(description = "Cantidad de registros exitosos")
        int exitosos,
        @Schema(description = "Cantidad de registros duplicados")
        int duplicados,
        @Schema(description = "Cantidad de registros sin precio")
        int sinPrecio,
        @Schema(description = "Mensajes de registros duplicados")
        List<String> mensajesDuplicados,
        @Schema(description = "Mensajes de registros sin precio")
        List<String> mensajesSinPrecio
) {}
