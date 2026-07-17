package com.laveronica.siscontrol.domain.categoria.validaciones.categoria;

import com.laveronica.siscontrol.domain.categoria.dto.DatosDetalleCategoria;
import com.laveronica.siscontrol.infra.exceptions.ex.RuleValidationException;
import com.laveronica.siscontrol.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarExisteCategoria implements ValiadacionesCategorias {

    private final CategoriaRepository categoriaRepository;

    @Override
    public void validar(DatosDetalleCategoria datos) {
        if (!categoriaRepository.existsById(datos.id())){
            throw new RuleValidationException("La categoria no existe seleccionada");
        }
    }
}