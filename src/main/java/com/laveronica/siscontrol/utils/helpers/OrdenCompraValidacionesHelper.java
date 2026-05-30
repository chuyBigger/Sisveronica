package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrdenCompraValidacionesHelper {

    @Autowired
    private OrdenCompraRespository ordenCompraRespository;


    public void validaOrdenCompraExiste(LocalDate fecha, Partida partida) {
        if (ordenCompraRespository.existsByPartidaAndFechaInicioSemanaAndActivoTrue(partida, fecha)){
            throw new RecursoExistenteException("Ya existe una Orden de compra con esta partida: " + partida + "y esta fecha :" +fecha);
        }
    }


    //todo es el mimso +
    public OrdenCompra buscarOrdenCompraId(String id) {
        System.out.println(id);
        return ordenCompraRespository.findByIdAndActivoTrue(id).orElseThrow(
                () -> new ResourceNotFoundException("Orden de compra no encontrada o el id no es valido: "+ id)
        );
    }
}
