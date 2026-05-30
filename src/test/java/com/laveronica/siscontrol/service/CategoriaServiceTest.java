package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosActualizarCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosDetalleCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.repositories.CategoriaRepository;
import com.laveronica.siscontrol.services.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void registrarCategoriaSuccess() {
        var datos = new DatosRegistroCategoria("Lacteos", Partida.LACTEOS);

        given(categoriaRepository.existsByNombre("Lacteos")).willReturn(false);
        given(categoriaRepository.save(any())).willAnswer(invocation -> {
            Categoria c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Categoria result = categoriaService.registrarCategoria(datos);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Lacteos");
    }

    @Test
    void registrarCategoriaThrowsRecursoExistenteException() {
        var datos = new DatosRegistroCategoria("Lacteos", Partida.LACTEOS);

        given(categoriaRepository.existsByNombre("Lacteos")).willReturn(true);

        assertThatThrownBy(() -> categoriaService.registrarCategoria(datos))
                .isInstanceOf(RecursoExistenteException.class)
                .hasMessageContaining("ya existe");
    }

    @Test
    void listaCategoriasReturnsFilteredList() {
        Categoria cat1 = new Categoria();
        cat1.setId(1L);
        cat1.setNombre("Lacteos");
        cat1.setPartida(Partida.LACTEOS);
        cat1.setActivo(true);

        Categoria cat2 = new Categoria();
        cat2.setId(2L);
        cat2.setNombre("Carnes");
        cat2.setPartida(Partida.CARNES);
        cat2.setActivo(true);

        given(categoriaRepository.findAll()).willReturn(List.of(cat1, cat2));

        List<DatosDetalleCategoria> result = categoriaService.listaCategorias();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).nombre()).isEqualTo("Lacteos");
    }

    @Test
    void eliminarCategoriaSetsActivoFalse() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        categoria.setActivo(true);

        given(categoriaRepository.findById(1L)).willReturn(Optional.of(categoria));

        categoriaService.eliminarCategoria(1L);

        assertThat(categoria.getActivo()).isFalse();
        verify(categoriaRepository).findById(1L);
    }
}
