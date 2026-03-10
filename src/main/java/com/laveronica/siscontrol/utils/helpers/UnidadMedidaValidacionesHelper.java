package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.enums.UnidadMedida;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UnidadMedidaValidacionesHelper {

    public UnidadMedida validar(String unidadMedida) {
        if (unidadMedida == null) {
            throw new ResourceNotFoundException("La Categoria es requerida y no puede estar vacía.");
        }
        String unidadMedidaNormalizada = unidadMedida.toUpperCase().trim();
        UnidadMedida unidadMedidaEnum;
        try {
            unidadMedidaEnum = UnidadMedida.valueOf(unidadMedidaNormalizada);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("La unidad de medida que intenta ingresar" + unidadMedida + "no es valida o no esta registrada");
        }
        return unidadMedidaEnum;
    }

}
