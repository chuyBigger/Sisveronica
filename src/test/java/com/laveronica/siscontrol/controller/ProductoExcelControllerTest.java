package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.productos.dto.DatosReporteCargaProductos;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.ProductoExcelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProductoExcelController.class)
class ProductoExcelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoExcelService productoExcelService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void cargarExcel_DeberiaRetornar200() throws Exception {
        var file = new MockMultipartFile("archivo", "productos.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "contenido del excel".getBytes());
        var response = new DatosReporteCargaProductos(1, 1, 0, 0, List.of(), List.of());

        given(productoExcelService.cargarProductosDesdeExcel(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/productos/excel/cargar")
                        .file(file)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.exitosos").value(1));
    }

    @Test
    @WithMockUser
    void descargarPlantilla_DeberiaRetornar200() throws Exception {
        byte[] plantilla = "contenido plantilla".getBytes();

        given(productoExcelService.generarPlantillaExcel()).willReturn(plantilla);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/excel/plantilla"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }
}
