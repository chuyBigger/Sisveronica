package com.laveronica.siscontrol.domain.contratos.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "ContratoActualizar")
public record DatosActualizarContrato(

        @Schema(description = "Identificador único del cliente")
        String clienteId,
        @Schema(description = "Fecha de inicio del contrato")
        LocalDate fechaInicio,
        @Schema(description = "Fecha de término del contrato")
        LocalDate fechaTermino,
        @Schema(description = "Monto del presupuesto")
        BigDecimal presupuesto
) {
}
