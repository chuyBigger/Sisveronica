package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosActualizarCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosDetalleCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.repositories.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria registrarCategoria(DatosRegistroCategoria datos) {
        if (datos.partida() == null) {
            throw new IllegalArgumentException("⚠️ Falta asignar 'partida'");
        }
        String nombreNormalizado = datos.nombre().toUpperCase().trim();
        if (!categoriaRepository.existsByNombre(nombreNormalizado)) {
            Categoria nuevaCategoria = new Categoria(
                    new DatosRegistroCategoria(nombreNormalizado, datos.partida()));
            categoriaRepository.save(nuevaCategoria);
            return nuevaCategoria;
        } else {
            throw new RecursoExistenteException("⚠️ Error el registro ya existe. ");
        }
    }

    public List<DatosDetalleCategoria> listaCategorias() {
        return categoriaRepository.findAll()
                // Mét_odo simple porque el máximo de categorías es de 20, no hace falta paginación
                .stream()
                .filter(Categoria::getActivo)
                .map(DatosDetalleCategoria::new)
                .toList();
    }

    public Categoria buscarCategoriaId(String id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La categoría seleccionada no existe"));
        return categoria;
    }

    @Transactional
    public Categoria actualizarCategoria(String id, @Valid DatosActualizarCategoria datos) {
        Categoria categoria = categoriaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("La Categoría seleccionada no existe"));
        if (datos.nombre() != null && !datos.nombre().isEmpty()) {
            categoria.setNombre(datos.nombre().toUpperCase().trim());
        }
        if (datos.partida() != null) {
            categoria.setPartida(datos.partida());
        }
        return categoria;
    }

    @Transactional
    public void eliminarCategoria(String id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("La categoría seleccionada no existe"));
        categoria.setActivo(false);
    }
}
