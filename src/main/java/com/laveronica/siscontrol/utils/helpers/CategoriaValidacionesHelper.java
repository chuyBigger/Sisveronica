package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.repositories.CategoriaRepository;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoriaValidacionesHelper {

    private final CategoriaRepository categoriaRepository;

    public Categoria validarCategoriaActiva(String id){

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("La categoría ingresada (ID: " + id + ") no existe."));
        if (!categoria.getActivo()){
            throw new ResourceNotFoundException("⚠️ La categoría: " + categoria.getNombre() + " se encuentra inactiva o eliminada.");
        }
        return categoria;
    }
}
