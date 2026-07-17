package com.laveronica.siscontrol.domain.productos.validaciones;

import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.infra.exceptions.ex.RuleValidationException;
import com.laveronica.siscontrol.repositories.ProductosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidacionNombreNoExista implements ValidadorDeProductos {

    private final ProductosRepository productosRepository;

    @Override
    public void validar(DatosRegistroProducto datos){
        if (productosRepository.existsByNombre(datos.nombre())){
            throw new RuleValidationException("El nombre del producto '"+datos.nombre()+"' ya se encuentra registrado");
        }
    }
}

