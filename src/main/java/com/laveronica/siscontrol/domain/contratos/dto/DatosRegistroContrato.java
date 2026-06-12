package com.laveronica.siscontrol.domain.contratos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "ContratoRegistro")
public record DatosRegistroContrato(

        @NotNull
        @Schema(description = "Nombre del contrato")
        String contrato,
        @NotNull
        @Positive
        @Schema(description = "Identificador único del cliente")
        String clienteId,
        @NotNull
        @Schema(description = "Fecha de inicio del contrato")
        LocalDate fechaInicio,
        @NotNull
        @Future
        @Schema(description = "Fecha de término del contrato")
        LocalDate fechaTermino,
        @NotNull
        @Positive
        @Digits(integer = 10, fraction = 2)
        @Schema(description = "Monto del presupuesto")
        BigDecimal presupuesto

) {

}
