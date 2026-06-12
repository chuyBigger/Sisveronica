package com.laveronica.siscontrol.domain.ordencompra.dto;

import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "OrdenCompraRegistro")
public record DatosRegistroOrdenCompra(

        @Schema(description = "Identificador único del cliente")
        String cliente_id,
        @Schema(description = "Identificador único del contrato")
        String contrato_id,
        @Schema(description = "Partida presupuestal")
        String partida,
        @Schema(description = "Fecha de inicio de la semana")
        LocalDate fechaInicioSemana,
        List<DatosRegistroOrdenCompraDetalle> detalles

) {
}
