package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosActualizarOrdenCompraDetalle;

import java.time.LocalDate;
import java.util.List;

public record DatosActulizarOrdenCompra(

        Long clienteId,
        Long contrato_id,
        String partida,
        LocalDate fechaInicioSemana,
        List<DatosActualizarOrdenCompraDetalle> detalles

) {
}
