package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosActualizarContrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosDetalleContrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosRegistroContrato;
import com.laveronica.siscontrol.services.ContratoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(ContratosController.class)
class ContratosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContratoService contratoService;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registrar() throws Exception {
        var request = new DatosRegistroContrato("CON-001", "1", LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000));
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");
        Contrato contrato = Contrato.builder()
                .id("1").contrato("CON-001").cliente(cliente)
                .fechaInicio(LocalDate.now())
                .fechaTermino(LocalDate.now().plusDays(30))
                .presupuesto(BigDecimal.valueOf(5000))
                .build();

        given(contratoService.registrarContrato(any())).willReturn(contrato);

        mockMvc.perform(MockMvcRequestBuilders.post("/contratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void contratosLista() throws Exception {
        var detalle = new DatosDetalleContrato("1", "CON-001", "Cliente Test", LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000));

        given(contratoService.listarContratos()).willReturn(List.of(detalle));

        mockMvc.perform(MockMvcRequestBuilders.get("/contratos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].contrato").value("CON-001"));
    }

    @Test
    void contratoBuscarId() throws Exception {
        var detalle = new DatosDetalleContrato("1", "CON-001", "Cliente Test", LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000));

        given(contratoService.buscarContratoId("1")).willReturn(detalle);

        mockMvc.perform(MockMvcRequestBuilders.get("/contratos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contrato").value("CON-001"));
    }

    @Test
    void actualizarContrato() throws Exception {
        var request = new DatosActualizarContrato("1", LocalDate.now(), LocalDate.now().plusDays(60), BigDecimal.valueOf(8000));
        var response = new DatosDetalleContrato("1", "CON-001", "Cliente Test", LocalDate.now(), LocalDate.now().plusDays(60), BigDecimal.valueOf(8000));

        given(contratoService.actualizarContratoId(any(), any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/contratos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.presupuesto").value(8000));
    }

    @Test
    void eliminarContrato() throws Exception {
        doNothing().when(contratoService).eliminarContrato("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/contratos/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
