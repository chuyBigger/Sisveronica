package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import java.time.LocalDate;
import java.util.List;

public record DatosRegistroOrdenCompra(

        String cliente_id,
        String contrato_id,
        String partida,
        LocalDate fechaInicioSemana,
        List<DatosRegistroOrdenCompraDetalle> detalles

) {
}
