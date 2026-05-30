package com.laveronica.siscontrol.domain.contratos.dto;

import com.laveronica.siscontrol.domain.contratos.Contrato;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DatosDetalleContrato(
        String id,
        String contrato,
        String cliente,
        LocalDate fechaInicio,
        LocalDate fechaTermino,
        BigDecimal presupuesto

        ) {
    public DatosDetalleContrato(Contrato contrato){
        this(
                contrato.getId(),
                contrato.getContrato(),
                contrato.getCliente().getNombre(),
                contrato.getFechaInicio(),
                contrato.getFechaTermino(),
                contrato.getPresupuesto()
        );
    }
}
