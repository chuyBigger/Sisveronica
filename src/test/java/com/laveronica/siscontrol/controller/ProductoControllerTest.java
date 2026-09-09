package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laveronica.siscontrol.domain.productos.dto.DatosActualizarProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosDetalleProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosListarProductos;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.enums.UnidadMedida;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.ProductoService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void registrar_DeberiaRetornar201() throws Exception {
        var request = new DatosRegistroProducto("Leche Entera", "LACTEOS", "1", UnidadMedida.LITRO, BigDecimal.valueOf(15), BigDecimal.valueOf(22), null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        var response = new DatosDetalleProducto("uuid-1", "leche entera", "LACTEOS", "1", "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001", null, null, null, null, null, null, null, null, null, null, null, null, null);

        given(productoService.registrarProducto(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("uuid-1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("leche entera"));
    }

    @Test
    @WithMockUser
    void listarProductos_DeberiaRetornar200() throws Exception {
        var producto = new DatosListarProductos("uuid-1", "leche entera", "LACTEOS", "Lacteos", "PROD-001", BigDecimal.valueOf(22), "PIEZA", null, null);
        Page<DatosListarProductos> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 9), 1);

        given(productoService.listaProductos(any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("leche entera"));
    }

    @Test
    @WithMockUser
    void listarProductosPartida_DeberiaRetornar200() throws Exception {
        var producto = new DatosListarProductos("uuid-1", "leche entera", "LACTEOS", "Lacteos", "PROD-001", BigDecimal.valueOf(22), "PIEZA", null, null);
        Page<DatosListarProductos> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 10), 1);

        given(productoService.listaProductosPartida(any(), any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/partidas/LACTEOS"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].partida").value("LACTEOS"));
    }

    @Test
    @WithMockUser
    void listarProductosCategoria_DeberiaRetornar200() throws Exception {
        var producto = new DatosListarProductos("uuid-1", "leche entera", "LACTEOS", "Lacteos", "PROD-001", BigDecimal.valueOf(22), "PIEZA", null, null);
        Page<DatosListarProductos> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 10), 1);

        given(productoService.listaProdictosCategoriaId(any(), any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/categorias/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].categoria").value("Lacteos"));
    }

    @Test
    @WithMockUser
    void buscarProductoId_DeberiaRetornar200() throws Exception {
        var response = new DatosDetalleProducto("uuid-1", "leche entera", "LACTEOS", "1", "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001", null, null, null, null, null, null, null, null, null, null, null, null, null);

        given(productoService.buscarProductoId("uuid-1")).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/uuid-1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("uuid-1"));
    }

    @Test
    @WithMockUser
    void buscarProductoNombre_DeberiaRetornar200() throws Exception {
        var response = new DatosDetalleProducto("uuid-1", "leche entera", "LACTEOS", "1", "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001", null, null, null, null, null, null, null, null, null, null, null, null, null);

        given(productoService.buscarProductoNombre("leche")).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar/leche"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("leche entera"));
    }

    @Test
    @WithMockUser
    void buscarProductosPorPalabra_DeberiaRetornar200() throws Exception {
        var response = new DatosDetalleProducto("uuid-1", "leche entera", "LACTEOS", "1", "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001", null, null, null, null, null, null, null, null, null, null, null, null, null);
        Page<DatosDetalleProducto> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        given(productoService.buscarProductosPorPalabra(any(), any())).willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar_palabras?q=leche"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("leche entera"));
    }

    @Test
    @WithMockUser
    void actualizarProducto_DeberiaRetornar200() throws Exception {
        var request = new DatosActualizarProducto("Leche Deslactosada", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        var response = new DatosDetalleProducto("uuid-1", "leche deslactosada", "LACTEOS", "1", "LITRO", BigDecimal.valueOf(15), BigDecimal.valueOf(22), "PROD-001", null, null, null, null, null, null, null, null, null, null, null, null, null);

        given(productoService.actualizarProductoId(any(), any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/productos/uuid-1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("leche deslactosada"));
    }

    @Test
    @WithMockUser
    void eliminarProducto_DeberiaRetornar204() throws Exception {
        doNothing().when(productoService).eliminarProducto("uuid-1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/productos/uuid-1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
