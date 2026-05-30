package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosActulizarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosDetalleOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosListarDetalleOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.services.OrdenCompraService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(OrdenCompraController.class)
class OrdenCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrdenCompraService ordenCompraService;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registrarOrdenCompra() throws Exception {
        var detalle = new DatosRegistroOrdenCompraDetalle(LocalDate.now(), 1L, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var request = new DatosRegistroOrdenCompra(1L, 1L, "ABARROTES", LocalDate.now(), List.of(detalle));
        var detalleResponse = new DatosDetalleOrdenCompraDetalle(1L, 1L, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var response = new DatosDetalleOrdenCompra(1L, "Cliente Test", "CON-001", Partida.ABARROTES, LocalDate.now(), List.of(detalleResponse));

        given(ordenCompraService.registrarOrdenCompra(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/orden_compra")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void listarOrdenCompra() throws Exception {
        var detalle = new DatosListarDetalleOrdenCompraDetalle(1L, 1L, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var orden = new DatosListarOrdenCompra(1L, "Cliente Test", "CON-001", "ABARROTES", LocalDate.now(), List.of(detalle));
        Page<DatosListarOrdenCompra> page = new PageImpl<>(List.of(orden), PageRequest.of(0, 9), 1);

        given(ordenCompraService.listarOrdenesCompra(any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/orden_compra"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].cliente").value("Cliente Test"));
    }

    @Test
    void buscarOrdenCompraId() throws Exception {
        var detalle = new DatosDetalleOrdenCompraDetalle(1L, 1L, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var response = new DatosDetalleOrdenCompra(1L, "Cliente Test", "CON-001", Partida.ABARROTES, LocalDate.now(), List.of(detalle));

        given(ordenCompraService.buscarOrdenCompraId(1L)).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/orden_compra/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void actilizarOdrdenCompra() throws Exception {
        var request = new DatosActulizarOrdenCompra(1L, 1L, "ABARROTES", LocalDate.now(), null);
        var detalle = new DatosDetalleOrdenCompraDetalle(1L, 1L, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var response = new DatosDetalleOrdenCompra(1L, "Cliente Test", "CON-001", Partida.ABARROTES, LocalDate.now(), List.of(detalle));

        given(ordenCompraService.actulizarOrdenCompraId(any(), any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/orden_compra/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void eliminarOrdenCompra() throws Exception {
        doNothing().when(ordenCompraService).eliminarOrdenCompra(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/orden_compra/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
