package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosActualizarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosRegistroNota;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaActualizarDetalle;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaDetalleRegistro;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaListarDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.services.NotaVentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(NotaVentaController.class)
class NotaVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotaVentaService notaVentaService;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registrarNota() throws Exception {
        var detalleRegistro = new NotaVentaDetalleRegistro(5, "1");
        var request = new DatosRegistroNota(1L, "ABARROTES", List.of(detalleRegistro));
        var detalleListar = new NotaVentaListarDetalle(5, "arroz", BigDecimal.valueOf(20), BigDecimal.valueOf(100));
        var response = new DatosDetalleNota(1L, LocalDateTime.now(), "Cliente Test", Partida.ABARROTES, List.of(detalleListar), BigDecimal.valueOf(100));

        given(notaVentaService.registrarNota(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/notaventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void listaNotas() throws Exception {
        var detalle = new NotaVentaListarDetalle(5, "arroz", BigDecimal.valueOf(20), BigDecimal.valueOf(100));
        var nota = new DatosListarNota(1L, LocalDateTime.now(), "Cliente Test", "ABARROTES", List.of(detalle), BigDecimal.valueOf(100));
        Page<DatosListarNota> page = new PageImpl<>(List.of(nota), PageRequest.of(0, 9), 1);

        given(notaVentaService.listarNotas(any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/notaventas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].cliente").value("Cliente Test"));
    }

    @Test
    void buscarNotaId() throws Exception {
        var detalle = new NotaVentaListarDetalle(5, "arroz", BigDecimal.valueOf(20), BigDecimal.valueOf(100));
        var response = new DatosDetalleNota(1L, LocalDateTime.now(), "Cliente Test", Partida.ABARROTES, List.of(detalle), BigDecimal.valueOf(100));

        given(notaVentaService.buscarNotaId(1L)).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/notaventas/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void actualizarNota() throws Exception {
        var detalleActualizar = new NotaVentaActualizarDetalle(10, "arroz");
        var request = new DatosActualizarNota("ABARROTES", List.of(detalleActualizar));
        var detalleListar = new NotaVentaListarDetalle(10, "arroz", BigDecimal.valueOf(20), BigDecimal.valueOf(200));
        var response = new DatosDetalleNota(1L, LocalDateTime.now(), "Cliente Test", Partida.ABARROTES, List.of(detalleListar), BigDecimal.valueOf(200));

        given(notaVentaService.actualizarNota(any(), any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/notaventas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalGeneral").value(200));
    }

    @Test
    void eliminarNota() throws Exception {
        doNothing().when(notaVentaService).eliminarNota(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/notaventas/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
