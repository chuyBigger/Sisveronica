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
            c.setId("1");
            return c;
        });

        Categoria result = categoriaService.registrarCategoria(datos);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getNombre()).isEqualTo("Lacteos");
        assertThat(result.getPartida()).isEqualTo(Partida.LACTEOS);
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
    void registrarCategoriaPartidaNullThrowsIllegalArgumentException() {
        var datos = new DatosRegistroCategoria("Test", null);

        assertThatThrownBy(() -> categoriaService.registrarCategoria(datos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("partida");
    }

    @Test
    void listaCategoriasReturnsFilteredList() {
        Categoria cat1 = new Categoria();
        cat1.setId("1");
        cat1.setNombre("Lacteos");
        cat1.setPartida(Partida.LACTEOS);
        cat1.setActivo(true);

        Categoria cat2 = new Categoria();
        cat2.setId("2");
        cat2.setNombre("Carnes");
        cat2.setPartida(Partida.CARNES);
        cat2.setActivo(true);

        Categoria cat3 = new Categoria();
        cat3.setId("3");
        cat3.setNombre("Inactiva");
        cat3.setPartida(Partida.VARIOS);
        cat3.setActivo(false);

        given(categoriaRepository.findAll()).willReturn(List.of(cat1, cat2, cat3));

        List<DatosDetalleCategoria> result = categoriaService.listaCategorias();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).nombre()).isEqualTo("Lacteos");
        assertThat(result.get(1).nombre()).isEqualTo("Carnes");
    }

    @Test
    void buscarCategoriaIdFound() {
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        categoria.setPartida(Partida.LACTEOS);
        categoria.setActivo(true);

        given(categoriaRepository.findById("1")).willReturn(Optional.of(categoria));

        Categoria result = categoriaService.buscarCategoriaId("1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getNombre()).isEqualTo("Lacteos");
    }

    @Test
    void buscarCategoriaIdThrowsEntityNotFoundException() {
        given(categoriaRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.buscarCategoriaId("bad-id"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("no existe");
    }

    @Test
    void actualizarCategoriaSuccess() {
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        categoria.setPartida(Partida.LACTEOS);
        categoria.setActivo(true);

        var datos = new DatosActualizarCategoria("Carnes Frias", Partida.CARNES);

        given(categoriaRepository.findByIdAndActivoTrue("1")).willReturn(Optional.of(categoria));

        Categoria result = categoriaService.actualizarCategoria("1", datos);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Carnes Frias");
        assertThat(result.getPartida()).isEqualTo(Partida.CARNES);
    }

    @Test
    void actualizarCategoriaThrowsEntityNotFoundException() {
        var datos = new DatosActualizarCategoria("Nuevo", Partida.VARIOS);

        given(categoriaRepository.findByIdAndActivoTrue("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.actualizarCategoria("bad-id", datos))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("no existe");
    }

    @Test
    void eliminarCategoriaSuccess() {
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        categoria.setActivo(true);

        given(categoriaRepository.findById("1")).willReturn(Optional.of(categoria));

        categoriaService.eliminarCategoria("1");

        assertThat(categoria.getActivo()).isFalse();
        verify(categoriaRepository).findById("1");
    }

    @Test
    void eliminarCategoriaThrowsEntityNotFoundException() {
        given(categoriaRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.eliminarCategoria("bad-id"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("no existe");
    }
}
