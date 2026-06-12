package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.productos.dto.DatosActualizarProducto;
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
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
        var datos = new DatosRegistroProducto("leche", "LACTEOS", "1", UnidadMedida.LITRO, BigDecimal.TEN, BigDecimal.valueOf(20), null);
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        var detalle = new DatosDetalleProducto("uuid-1", "leche", "LACTEOS", "1", "LITRO", BigDecimal.TEN, BigDecimal.valueOf(20), "PROD-001");

        given(productoValidacionesHelper.validarNombreNoExista(datos)).willReturn("leche");
        given(partidaValidacionesHelper.validaPartidaExistaString("LACTEOS")).willReturn(Partida.LACTEOS);
        given(categoriaValidacionesHelper.validarCategoriaActiva("1")).willReturn(categoria);
        given(productoMapper.toEntity(datos, Partida.LACTEOS, categoria)).willReturn(producto);
        given(productosRepository.save(producto)).willReturn(producto);
        given(productoMapper.toDetalleDto(producto)).willReturn(detalle);

        var result = productoService.registrarProducto(datos);

        assertThat(result).isNotNull();
        assertThat(result.nombre()).isEqualTo("leche");
        verify(productosRepository).save(producto);
    }

    @Test
    void listaProductosReturnsPage() {
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        producto.setCategoria(categoria);
        producto.setCodigo("PROD-001");
        producto.setPrecioVenta(BigDecimal.valueOf(20));
        Page<Producto> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 9), 1);

        given(productosRepository.findAllByActivoTrue(any(Pageable.class))).willReturn(page);

        Page<DatosListarProductos> result = productoService.listaProductos(PageRequest.of(0, 9));

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void listaProductosPartidaReturnsPage() {
        Partida partida = Partida.CARNES;
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("carne");
        producto.setPartida(partida);
        producto.setPrecioVenta(BigDecimal.valueOf(50));
        Page<Producto> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 9), 1);

        given(partidaValidacionesHelper.validaPartidaExistaString("CARNES")).willReturn(partida);
        given(productosRepository.findAllByPartidaAndActivoTrue(partida, PageRequest.of(0, 9))).willReturn(page);

        Page<DatosListarProductos> result = productoService.listaProductosPartida(PageRequest.of(0, 9), "CARNES");

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void listaProdictosCategoriaIdReturnsPage() {
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        categoria.setActivo(true);
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        producto.setCategoria(categoria);
        producto.setPrecioVenta(BigDecimal.valueOf(20));
        Page<Producto> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 9), 1);

        given(categoriaValidacionesHelper.validarCategoriaActiva("1")).willReturn(categoria);
        given(productosRepository.findAllByCategoriaAndActivoTrue(categoria, PageRequest.of(0, 9))).willReturn(page);

        Page<DatosListarProductos> result = productoService.listaProdictosCategoriaId("1", PageRequest.of(0, 9));

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void buscarProductoIdFound() {
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Lacteos");
        producto.setCategoria(categoria);
        producto.setUnidadMedida(UnidadMedida.LITRO);
        producto.setPrecioVenta(BigDecimal.valueOf(20));
        producto.setPrecioCompra(BigDecimal.TEN);
        producto.setCodigo("PROD-001");

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

    @Test
    void buscarProductoNombreFound() {
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        producto.setUnidadMedida(UnidadMedida.LITRO);
        producto.setPrecioVenta(BigDecimal.valueOf(20));

        given(productosRepository.findByNombreAndActivoTrue("leche")).willReturn(Optional.of(producto));

        var result = productoService.buscarProductoNombre("leche");

        assertThat(result).isNotNull();
        assertThat(result.nombre()).isEqualTo("leche");
    }

    @Test
    void buscarProductoNombreThrowsResourceNotFoundException() {
        given(productosRepository.findByNombreAndActivoTrue("no-existe")).willReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.buscarProductoNombre("no-existe"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontró ningún producto");
    }

    @Test
    void buscarProductosPorPalabraReturnsPage() {
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        producto.setUnidadMedida(UnidadMedida.LITRO);
        producto.setPrecioVenta(BigDecimal.valueOf(20));
        Page<Producto> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 9), 1);

        given(productosRepository.findAllByNombreContainingAndActivoTrue(anyString(), any(Pageable.class))).willReturn(page);

        Page<DatosDetalleProducto> result = productoService.buscarProductosPorPalabra("lech", PageRequest.of(0, 9));

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void actualizarProductoIdSuccess() {
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setNombre("leche");
        producto.setPartida(Partida.LACTEOS);
        producto.setUnidadMedida(UnidadMedida.LITRO);
        producto.setPrecioCompra(BigDecimal.TEN);
        producto.setPrecioVenta(BigDecimal.valueOf(20));

        var datos = new DatosActualizarProducto("leche fresca", "CARNES", "2", "KILO", BigDecimal.valueOf(15), BigDecimal.valueOf(25), "LEC-001");
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setId("2");
        nuevaCategoria.setNombre("Carnes");

        given(productosRepository.findById("uuid-1")).willReturn(Optional.of(producto));
        given(partidaValidacionesHelper.validaPartidaExistaString("CARNES")).willReturn(Partida.CARNES);
        given(categoriaValidacionesHelper.validarCategoriaActiva("2")).willReturn(nuevaCategoria);
        given(unidadMedidaValidacionesHelper.validar("KILO")).willReturn(UnidadMedida.KILO);

        var result = productoService.actualizarProductoId("uuid-1", datos);

        assertThat(result).isNotNull();
        assertThat(producto.getNombre()).isEqualTo("leche fresca");
        assertThat(producto.getPartida()).isEqualTo(Partida.CARNES);
        assertThat(producto.getUnidadMedida()).isEqualTo(UnidadMedida.KILO);
        assertThat(producto.getPrecioCompra()).isEqualTo(BigDecimal.valueOf(15));
        assertThat(producto.getPrecioVenta()).isEqualTo(BigDecimal.valueOf(25));
    }

    @Test
    void actualizarProductoIdThrowsResourceNotFoundException() {
        var datos = new DatosActualizarProducto("nuevo", null, null, null, null, null, null);

        given(productosRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.actualizarProductoId("bad-id", datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontró ningún producto");
    }

    @Test
    void eliminarProductoSuccess() {
        Producto producto = new Producto();
        producto.setId("uuid-1");
        producto.setActivo(true);

        given(productosRepository.findById("uuid-1")).willReturn(Optional.of(producto));

        productoService.eliminarProducto("uuid-1");

        assertThat(producto.getActivo()).isFalse();
        verify(productosRepository).findById("uuid-1");
    }

    @Test
    void eliminarProductoThrowsResourceNotFoundException() {
        given(productosRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.eliminarProducto("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontró ningún producto");
    }
}
