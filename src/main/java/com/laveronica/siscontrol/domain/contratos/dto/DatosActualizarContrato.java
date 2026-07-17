package com.laveronica.siscontrol.domain.contratos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

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
        @Positive
        @Digits(integer = 10, fraction = 2)
        @Schema(description = "Monto del presupuesto")
        BigDecimal presupuesto,
        @Schema(description = "Referencia SPEI")
        String spei,
        @Schema(description = "Detalles adicionales del contrato")
        String detalles

) {

}
