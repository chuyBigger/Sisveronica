package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosActualizarCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosDetalleCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void registrar_DeberiaRetornar201() throws Exception {
        var request = new DatosRegistroCategoria("Lacteos", Partida.LACTEOS);
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        categoria.setPartida(Partida.LACTEOS);
        categoria.setActivo(true);

        given(categoriaService.registrarCategoria(any())).willReturn(categoria);

        mockMvc.perform(MockMvcRequestBuilders.post("/categorias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Lacteos"));
    }

    @Test
    @WithMockUser
    void listar_DeberiaRetornar200() throws Exception {
        var detalle = new DatosDetalleCategoria("1", "Lacteos", Partida.LACTEOS);

        given(categoriaService.listaCategorias()).willReturn(List.of(detalle));

        mockMvc.perform(MockMvcRequestBuilders.get("/categorias"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Lacteos"));
    }

    @Test
    @WithMockUser
    void buscarPorId_DeberiaRetornar200() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        categoria.setPartida(Partida.LACTEOS);

        given(categoriaService.buscarCategoriaId("1")).willReturn(categoria);

        mockMvc.perform(MockMvcRequestBuilders.get("/categorias/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser
    void actualizar_DeberiaRetornar200() throws Exception {
        var request = new DatosActualizarCategoria("Carnes Frias", Partida.CARNES);
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Carnes Frias");
        categoria.setPartida(Partida.CARNES);

        given(categoriaService.actualizarCategoria(any(), any())).willReturn(categoria);

        mockMvc.perform(MockMvcRequestBuilders.patch("/categorias/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Carnes Frias"));
    }

    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar204() throws Exception {
        doNothing().when(categoriaService).eliminarCategoria("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/categorias/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
