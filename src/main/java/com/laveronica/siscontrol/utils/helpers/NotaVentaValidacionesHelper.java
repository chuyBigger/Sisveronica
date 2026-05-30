package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotaVentaValidacionesHelper {

    @Autowired
    private NotaVentaRepository notaVentaRepository;

    public NotaVenta notaVentaExiste(String id){
        if (id == null ){
            throw new ResourceNotFoundException(" el valor id esta vacio o mal escrito :"+id);
        }
        NotaVenta notaEncontrada = notaVentaRepository.findByIdAndActivoTrue(id).orElseThrow(
                () -> new ResourceNotFoundException("⚠️ El id no coicide con ninguna nota")
        );
        return notaEncontrada;
    }
}
