package com.laveronica.siscontrol.domain.notaventadetalle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "NotaVentaDetalleInfo")
public record DatosDetalleNotaDetalle(
        String id,
        Integer cantidad,
        @Schema(description = "Nombre del producto")
        String producto,
        BigDecimal precio,
        @Schema(description = "Subtotal")
        BigDecimal SubTotal
) {
}
