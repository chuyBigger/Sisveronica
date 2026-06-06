package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@WebMvcTest(EnumsController.class)
class EnumsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void listarPartidas_DeberiaRetornar200() throws Exception {
        var partidas = Arrays.stream(Partida.values()).map(Enum::name).toList();

        mockMvc.perform(MockMvcRequestBuilders.get("/enums/partidas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(partidas.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("ABARROTES"));
    }

    @Test
    @WithMockUser
    void listarUnidadesMedida_DeberiaRetornar200() throws Exception {
        var unidades = Arrays.stream(UnidadMedida.values()).map(Enum::name).toList();

        mockMvc.perform(MockMvcRequestBuilders.get("/enums/unidades-medida"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(unidades.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("KILO"));
    }
}
