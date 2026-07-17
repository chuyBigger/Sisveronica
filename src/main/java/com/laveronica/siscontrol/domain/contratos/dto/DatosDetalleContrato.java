package com.laveronica.siscontrol.domain.contratos.dto;

import com.laveronica.siscontrol.domain.contratos.Contrato;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "ContratoDetalle")
public record DatosDetalleContrato(
        String id,
        @Schema(description = "Nombre del contrato")
        String contrato,
        String cliente,
        @Schema(description = "Fecha de inicio del contrato")
        LocalDate fechaInicio,
        @Schema(description = "Fecha de término del contrato")
        LocalDate fechaTermino,
        @Schema(description = "Monto del presupuesto")
        BigDecimal presupuesto,
        @Schema(description = "Referencia SPEI")
        String spei,
        @Schema(description = "Detalles adicionales del contrato")
        String detalles

        ) {
    public DatosDetalleContrato(Contrato contrato){
        this(
                contrato.getId(),
                contrato.getContrato(),
                contrato.getCliente().getNombre(),
                contrato.getFechaInicio(),
                contrato.getFechaTermino(),
                contrato.getPresupuesto(),
                contrato.getSpei(),
                contrato.getDetalles()
        );
    }
}
