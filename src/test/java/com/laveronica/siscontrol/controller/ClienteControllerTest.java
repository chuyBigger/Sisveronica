package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosActualizarCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosDetalleCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosRegistroCliente;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.ClienteService;
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

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void registrar_DeberiaRetornar201() throws Exception {
        var request = new DatosRegistroCliente("Cliente Test", "XAXX010101000", "Calle 1", 123, "Fracc", "12345", "Municipio", "Estado");
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        given(clienteService.registarCliente(any())).willReturn(cliente);

        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    void listar_DeberiaRetornar200() throws Exception {
        var detalle = new DatosDetalleCliente("1", "Cliente Test", "XAXX010101000", "Calle 1", 123, "Fracc", "12345", "Municipio", "Estado");

        given(clienteService.buscarTodos()).willReturn(List.of(detalle));

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Cliente Test"));
    }

    @Test
    @WithMockUser
    void buscarPorId_DeberiaRetornar200() throws Exception {
        var detalle = new DatosDetalleCliente("1", "Cliente Test", "XAXX010101000", "Calle 1", 123, "Fracc", "12345", "Municipio", "Estado");

        given(clienteService.buscarClienteId("1")).willReturn(detalle);

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser
    void actualizar_DeberiaRetornar200() throws Exception {
        var request = new DatosActualizarCliente("Cliente Actualizado", null, null, null, null, null, null);
        var detalle = new DatosDetalleCliente("1", "Cliente Actualizado", "XAXX010101000", "Calle 1", 123, "Fracc", "12345", "Municipio", "Estado");

        given(clienteService.actualizarCliente(any(), any())).willReturn(detalle);

        mockMvc.perform(MockMvcRequestBuilders.patch("/clientes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Cliente Actualizado"));
    }

    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar204() throws Exception {
        doNothing().when(clienteService).eliminarCliente("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
