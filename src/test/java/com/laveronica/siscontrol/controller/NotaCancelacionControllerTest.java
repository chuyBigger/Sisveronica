package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosListarCancelacion;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosRegistroCancelacion;
import com.laveronica.siscontrol.domain.notacancelaciondetalle.dto.DatosListarCancelacionDetalle;
import com.laveronica.siscontrol.domain.notacancelaciondetalle.dto.DatosRegistroCancelacionDetalle;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.NotaCancelacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(NotaCancelacionController.class)
class NotaCancelacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotaCancelacionService cancelacionService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void crear_DeberiaRetornar200() throws Exception {
        var detalleRegistro = new DatosRegistroCancelacionDetalle("1", 5.0);
        var request = new DatosRegistroCancelacion("1", "lunes", List.of(detalleRegistro));
        var detalleListar = new DatosListarCancelacionDetalle("1", "1", "Producto 1", 5.0);
        var response = new DatosListarCancelacion("1", "1", "lunes", LocalDateTime.now(), "user", null, null, List.of(detalleListar));

        given(cancelacionService.crearCancelacion(any(), anyString())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/cancelaciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dia").value("lunes"));
    }

    @Test
    @WithMockUser
    void listarPorOrden_DeberiaRetornar200() throws Exception {
        var detalle = new DatosListarCancelacionDetalle("1", "1", "Producto 1", 5.0);
        var response = new DatosListarCancelacion("1", "1", "lunes", LocalDateTime.now(), "user", null, null, List.of(detalle));

        given(cancelacionService.listarPorOrden("1")).willReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/cancelaciones/orden/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dia").value("lunes"));
    }

    @Test
    @WithMockUser
    void validar_DeberiaRetornar200() throws Exception {
        var detalle = new DatosListarCancelacionDetalle("1", "1", "Producto 1", 5.0);
        var response = new DatosListarCancelacion("1", "1", "lunes", LocalDateTime.now(), "user", "user", LocalDateTime.now(), List.of(detalle));

        given(cancelacionService.validarCancelacion(anyString(), anyString())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/cancelaciones/1/validar")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.validadoPor").value("user"));
    }

    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar204() throws Exception {
        doNothing().when(cancelacionService).eliminarCancelacion("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/cancelaciones/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    void reconstruir_DeberiaRetornar200() throws Exception {
        var response = new DatosDetalleNota("1", 100, LocalDateTime.now(), "Cliente Test", Partida.ABARROTES, List.of(), BigDecimal.valueOf(100), "lunes", false, null);

        given(cancelacionService.reconstruirNotas("1")).willReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/cancelaciones/reconstruir/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"));
    }
}
