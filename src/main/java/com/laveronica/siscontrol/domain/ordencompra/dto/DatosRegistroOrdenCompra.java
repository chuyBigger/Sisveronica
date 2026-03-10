package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.util.List;

public record DatosRegistroOrdenCompra(

        @Column(name = "cliente_id")
        Long cliente_id,
        Long contrato_id,
        String partida,
        LocalDate fechaInicioSemana,
        List<DatosRegistroOrdenCompraDetalle> detalles

) {
}
