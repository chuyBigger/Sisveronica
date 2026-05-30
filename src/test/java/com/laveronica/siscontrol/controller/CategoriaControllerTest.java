package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosActualizarCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosDetalleCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.services.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService categoriaService;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registrar() throws Exception {
        var request = new DatosRegistroCategoria("Lacteos", Partida.LACTEOS);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        categoria.setPartida(Partida.LACTEOS);
        categoria.setActivo(true);

        given(categoriaService.registrarCategoria(any())).willReturn(categoria);

        mockMvc.perform(MockMvcRequestBuilders.post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Lacteos"));
    }

    @Test
    void listarCategorias() throws Exception {
        var detalle = new DatosDetalleCategoria(1L, "Lacteos", Partida.LACTEOS);

        given(categoriaService.listaCategorias()).willReturn(List.of(detalle));

        mockMvc.perform(MockMvcRequestBuilders.get("/categorias"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Lacteos"));
    }

    @Test
    void buscarCategoriaId() throws Exception {
        var detalle = new DatosDetalleCategoria(1L, "Lacteos", Partida.LACTEOS);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        categoria.setPartida(Partida.LACTEOS);

        given(categoriaService.buscarCategoriaId(1L)).willReturn(categoria);

        mockMvc.perform(MockMvcRequestBuilders.get("/categorias/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void actualizaCategoriaid() throws Exception {
        var request = new DatosActualizarCategoria("Carnes Frias", Partida.CARNES);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Carnes Frias");
        categoria.setPartida(Partida.CARNES);

        given(categoriaService.actualizarCategoria(any(), any())).willReturn(categoria);

        mockMvc.perform(MockMvcRequestBuilders.patch("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Carnes Frias"));
    }

    @Test
    void eliminarCategoria() throws Exception {
        doNothing().when(categoriaService).eliminarCategoria(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/categorias/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
