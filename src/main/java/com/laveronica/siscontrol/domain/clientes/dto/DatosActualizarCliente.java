package com.laveronica.siscontrol.domain.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClienteActualizar")
public record DatosActualizarCliente(
        String nombre,
        String calle,
        Integer numero,
        @Schema(description = "Nombre del fraccionamiento o colonia")
        String fraccionamiento,
        String cp,
        String municipio,
        String estado
) {
}
