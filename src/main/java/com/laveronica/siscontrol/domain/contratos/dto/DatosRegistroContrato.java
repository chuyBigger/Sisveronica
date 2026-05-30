package com.laveronica.siscontrol.domain.contratos.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DatosRegistroContrato(

        @NotNull
        String contrato,
        @NotNull
        @Positive
        String clienteId,
        @NotNull
        LocalDate fechaInicio,
        @NotNull
        @Future
        LocalDate fechaTermino,
        @NotNull
        @Positive
        @Digits(integer = 10, fraction = 2)
        BigDecimal presupuesto

) {

}
