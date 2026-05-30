package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.productos.dto.DatosActualizarProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosDetalleProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosListarProductos;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.enums.UnidadMedida;
import com.laveronica.siscontrol.services.ProductoService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registrar() throws Exception {
        var request = new DatosRegistroProducto("Leche Entera", "LACTEOS", 1L, UnidadMedida.LITRO, BigDecimal.valueOf(15), BigDecimal.valueOf(22), null);
        var response = new DatosDetalleProducto("uuid-1", "leche entera", "LACTEOS", 1L, "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001");

        given(productoService.registrarProducto(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("uuid-1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("leche entera"));
    }

    @Test
    void listarPodructo() throws Exception {
        var producto = new DatosListarProductos("uuid-1", "leche entera", "LACTEOS", "Lacteos", "PROD-001", BigDecimal.valueOf(22));
        Page<DatosListarProductos> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 9), 1);

        given(productoService.listaProductos(any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("leche entera"));
    }

    @Test
    void buscarProductoId() throws Exception {
        var response = new DatosDetalleProducto("uuid-1", "leche entera", "LACTEOS", 1L, "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001");

        given(productoService.buscarProductoId("uuid-1")).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/uuid-1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("uuid-1"));
    }

    @Test
    void listarProductosPartida() throws Exception {
        var producto = new DatosListarProductos("uuid-1", "leche entera", "LACTEOS", "Lacteos", "PROD-001", BigDecimal.valueOf(22));
        Page<DatosListarProductos> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 10), 1);

        given(productoService.listaProductosPartida(any(), any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/partidas/LACTEOS"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].partida").value("LACTEOS"));
    }

    @Test
    void listarProductoCategoria() throws Exception {
        var producto = new DatosListarProductos("uuid-1", "leche entera", "LACTEOS", "Lacteos", "PROD-001", BigDecimal.valueOf(22));
        Page<DatosListarProductos> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 10), 1);

        given(productoService.listaProdictosCategoriaId(any(), any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/categorias/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].categoria").value("Lacteos"));
    }

    @Test
    void buscarProductosPorPalabra() throws Exception {
        var response = new DatosDetalleProducto("uuid-1", "leche entera", "LACTEOS", 1L, "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001");
        Page<DatosDetalleProducto> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        given(productoService.buscarProductosPorPalabra(any(), any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar_palabras?q=leche"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("leche entera"));
    }

    @Test
    void actualizarProductoId() throws Exception {
        var request = new DatosActualizarProducto("Leche Deslactosada", null, null, null, null, null, null);
        var response = new DatosDetalleProducto("uuid-1", "leche deslactosada", "LACTEOS", 1L, "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001");

        given(productoService.actualizarProductoId(any(), any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("leche deslactosada"));
    }

    @Test
    void eliminarProducto() throws Exception {
        doNothing().when(productoService).eliminarProducto("uuid-1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/productos/uuid-1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
