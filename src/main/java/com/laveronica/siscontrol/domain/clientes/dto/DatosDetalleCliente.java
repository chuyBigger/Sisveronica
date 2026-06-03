package com.laveronica.siscontrol.domain.clientes.dto;


import com.laveronica.siscontrol.domain.clientes.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClienteDetalle")
public record DatosDetalleCliente(
        String id,
        String nombre,
        @Schema(description = "Registro Federal de Contribuyentes")
        String rfc,
        String calle,
        Integer numero,
        @Schema(description = "Nombre del fraccionamiento o colonia")
        String fraccionamiento,
        String cp,
        String municipio,
        String estado
) {
    public DatosDetalleCliente(Cliente cliente){
        this(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getRfc(),
                cliente.getCalle(),
                cliente.getNumero(),
                cliente.getFraccionamiento(),
                cliente.getCp(),
                cliente.getMunicipio(),
                cliente.getEstado()
        );
    }
}
