package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@WebMvcTest(EnumsController.class)
class EnumsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarPartidas() throws Exception {
        var partidas = Arrays.stream(Partida.values()).map(Enum::name).toList();

        mockMvc.perform(MockMvcRequestBuilders.get("/enums/partidas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(partidas.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("ABARROTES"));
    }

    @Test
    void listarUnidadesMedida() throws Exception {
        var unidades = Arrays.stream(UnidadMedida.values()).map(Enum::name).toList();

        mockMvc.perform(MockMvcRequestBuilders.get("/enums/unidades-medida"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(unidades.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("KILO"));
    }
}
