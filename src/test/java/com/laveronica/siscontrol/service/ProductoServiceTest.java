package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.productos.dto.DatosDetalleProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosListarProductos;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.domain.productos.productomapper.ProductoMapper;
import com.laveronica.siscontrol.domain.productos.validaciones.ValidadorDeProductos;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.ProductosRepository;
import com.laveronica.siscontrol.services.ProductoService;
import com.laveronica.siscontrol.utils.helpers.CategoriaValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.UnidadMedidaValidacionesHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoMapper productoMapper;

    @Mock
    private PartidaValidacionesHelper partidaValidacionesHelper;

    @Mock
    private CategoriaValidacionesHelper categoriaValidacionesHelper;

    @Mock
    private UnidadMedidaValidacionesHelper unidadMedidaValidacionesHelper;

    @Mock
    private List<ValidadorDeProductos> validadores;

    @Mock
    private ProductosRepository productosRepository;

    @Mock
    private ProductoValidacionesHelper productoValidacionesHelper;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void registrarProductoSuccess() {
        var datos = new DatosRegistroProducto("leche", "LACTEOS", 1L, UnidadMedida.LITRO, BigDecimal.TEN, BigDecimal.valueOf(20), null);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        var detalle = new DatosDetalleProducto("uuid-1", "leche", "LACTEOS", 1L, "LITRO", BigDecimal.TEN, BigDecimal.valueOf(20), "PROD-001");

        given(productoValidacionesHelper.validarNombreNoExista(datos)).willReturn("leche");
        given(partidaValidacionesHelper.validaPartidaExistaString("LACTEOS")).willReturn(Partida.LACTEOS);
        given(categoriaValidacionesHelper.validarCategoriaActiva(1L)).willReturn(categoria);
        given(productoMapper.toEntity(datos, Partida.LACTEOS, categoria)).willReturn(producto);
        given(productosRepository.save(producto)).willReturn(producto);
        given(productoMapper.toDetalleDto(producto)).willReturn(detalle);

        var result = productoService.registrarProducto(datos);

        assertThat(result).isNotNull();
        assertThat(result.nombre()).isEqualTo("leche");
    }

    @Test
    void listaProductosReturnsPage() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        producto.setCategoria(categoria);
        Page<Producto> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 9), 1);

        given(productosRepository.findAllByActivoTrue(any())).willReturn(page);

        Page<DatosListarProductos> result = productoService.listaProductos(PageRequest.of(0, 9));

        assertThat(result).isNotEmpty();
    }

    @Test
    void buscarProductoIdFound() {
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        producto.setCategoria(categoria);
        producto.setUnidadMedida(UnidadMedida.LITRO);
        producto.setPrecioVenta(BigDecimal.valueOf(20));

        given(productosRepository.findByIdAndActivoTrue("uuid-1")).willReturn(Optional.of(producto));

        var result = productoService.buscarProductoId("uuid-1");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("uuid-1");
    }

    @Test
    void buscarProductoIdThrowsResourceNotFoundException() {
        given(productosRepository.findByIdAndActivoTrue("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.buscarProductoId("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no corresponde a ningun producto");
    }
}
