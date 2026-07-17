package com.laveronica.siscontrol.domain.categoria.validaciones.registro;

import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.infra.exceptions.ex.RuleValidationException;
import com.laveronica.siscontrol.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidarNombreCategoria implements ValiadacionesRegistarCategorias {

    private final CategoriaRepository categoriaRepository;

    @Override
    public void validar(DatosRegistroCategoria datos) {
        if (categoriaRepository.existsByNombre(datos.nombre())){
            throw new RuleValidationException("ya existe una categoria registrada con un nombre: "+ datos.nombre());
        }
    }
}