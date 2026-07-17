package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OrdenCompraValidacionesHelper {

    private final OrdenCompraRespository ordenCompraRespository;


    public void validaOrdenCompraExiste(String clienteId, LocalDate fecha, Partida partida) {
        if (ordenCompraRespository.existsByCliente_IdAndPartidaAndFechaInicioSemanaAndActivoTrue(clienteId, partida, fecha)){
            throw new RecursoExistenteException("Ya existe una orden de compra para este cliente, partida " + partida + " y semana " + fecha);
        }
    }

    public void validaOrdenCompraExisteAlActualizar(String id, String clienteId, LocalDate fecha, Partida partida) {
        OrdenCompra actual = buscarOrdenCompraId(id);
        boolean mismoCliente = actual.getCliente().getId().equals(clienteId);
        boolean mismaPartida = actual.getPartida().equals(partida);
        boolean mismaSemana = actual.getFechaInicioSemana().equals(fecha);
        if (mismoCliente && mismaPartida && mismaSemana) {
            return;
        }
        validaOrdenCompraExiste(clienteId, fecha, partida);
    }


    //todo es el mimso +
    public OrdenCompra buscarOrdenCompraId(String id) {
        System.out.println(id);
        return ordenCompraRespository.findByIdAndActivoTrue(id).orElseThrow(
                () -> new ResourceNotFoundException("Orden de compra no encontrada o el id no es valido: "+ id)
        );
    }
}
