package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaListarDetalle;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosActulizarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosActualizarOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosDetalleOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosListarDetalleOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.OrdenCompraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(OrdenCompraController.class)
class OrdenCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenCompraService ordenCompraService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void registrar_DeberiaRetornar201() throws Exception {
        var detalle = new DatosRegistroOrdenCompraDetalle(LocalDate.now(), "1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var request = new DatosRegistroOrdenCompra("1", "1", "ABARROTES", LocalDate.now(), List.of(detalle));
        var detalleResponse = new DatosDetalleOrdenCompraDetalle("1", "1", "Producto 1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var response = new DatosDetalleOrdenCompra("1", "Cliente Test", "CON-001", Partida.ABARROTES, LocalDate.now(), List.of(detalleResponse), null, null);

        given(ordenCompraService.registrarOrdenCompra(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/orden_compra")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser
    void listar_DeberiaRetornar200() throws Exception {
        var detalle = new DatosListarDetalleOrdenCompraDetalle("1", "1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var orden = new DatosListarOrdenCompra("1", "Cliente Test", "CON-001", "ABARROTES", LocalDate.now(), List.of(detalle), null, null, false, "PENDIENTE", 0L, 0L, 0L, 0L);
        Page<DatosListarOrdenCompra> page = new PageImpl<>(List.of(orden), PageRequest.of(0, 9), 1);

        given(ordenCompraService.listarOrdenesCompra(any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/orden_compra"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].cliente").value("Cliente Test"));
    }

    @Test
    @WithMockUser
    void buscarPorId_DeberiaRetornar200() throws Exception {
        var detalle = new DatosDetalleOrdenCompraDetalle("1", "1", "Producto 1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var response = new DatosDetalleOrdenCompra("1", "Cliente Test", "CON-001", Partida.ABARROTES, LocalDate.now(), List.of(detalle), null, null);

        given(ordenCompraService.buscarOrdenCompraId("1")).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/orden_compra/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser
    void actualizar_DeberiaRetornar200() throws Exception {
        var detalleActualizar = new DatosActualizarOrdenCompraDetalle("1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var request = new DatosActulizarOrdenCompra("1", "1", "ABARROTES", LocalDate.now(), List.of(detalleActualizar));
        var detalle = new DatosDetalleOrdenCompraDetalle("1", "1", "Producto 1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var response = new DatosDetalleOrdenCompra("1", "Cliente Test", "CON-001", Partida.ABARROTES, LocalDate.now(), List.of(detalle), null, null);

        given(ordenCompraService.actulizarOrdenCompraId(any(), any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/orden_compra/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar204() throws Exception {
        doNothing().when(ordenCompraService).eliminarOrdenCompra("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/orden_compra/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    void confirmar_DeberiaRetornar200() throws Exception {
        var detalle = new DatosDetalleOrdenCompraDetalle("1", "1", "Producto 1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var response = new DatosDetalleOrdenCompra("1", "Cliente Test", "CON-001", Partida.ABARROTES, LocalDate.now(), List.of(detalle), "user", LocalDateTime.now());

        given(ordenCompraService.confirmarOrdenCompra(anyString(), anyString())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/orden_compra/1/confirmar")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.confirmadoPor").value("user"));
    }

    @Test
    @WithMockUser
    void listarNotasPorOrden_DeberiaRetornar200() throws Exception {
        var detalle = new NotaVentaListarDetalle(5, "arroz", BigDecimal.valueOf(20), BigDecimal.valueOf(100));
        var nota = new DatosListarNota("1", 100, LocalDateTime.now(), "Cliente Test", "ABARROTES", List.of(detalle), BigDecimal.valueOf(100), "lunes", false, null);

        given(ordenCompraService.listarNotasPorOrden("1")).willReturn(List.of(nota));

        mockMvc.perform(MockMvcRequestBuilders.get("/orden_compra/1/notas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cliente").value("Cliente Test"));
    }

    @Test
    @WithMockUser
    void generarTodasNotas_DeberiaRetornar200() throws Exception {
        var response = new DatosDetalleNota("1", 100, LocalDateTime.now(), "Cliente Test", Partida.ABARROTES, List.of(), BigDecimal.valueOf(100), "lunes", false, null);

        given(ordenCompraService.generarTodasNotas("1")).willReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/orden_compra/1/generar-notas")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"));
    }
}
