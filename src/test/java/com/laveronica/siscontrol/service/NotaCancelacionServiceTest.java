package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosListarCancelacion;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosRegistroCancelacion;
import com.laveronica.siscontrol.domain.notacancelaciondetalle.NotaCancelacionDetalle;
import com.laveronica.siscontrol.domain.notacancelaciondetalle.dto.DatosRegistroCancelacionDetalle;
import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.NotaCancelacionRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.services.NotaCancelacionService;
import com.laveronica.siscontrol.services.NotaVentaDetalleService;
import com.laveronica.siscontrol.utils.helpers.OrdenCompraValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotaCancelacionServiceTest {

    @Mock
    private NotaCancelacionRepository cancelacionRepository;

    @Mock
    private OrdenCompraValidacionesHelper ordenCompraValidacionesHelper;

    @Mock
    private OrdenCompraRespository ordenCompraRespository;

    @Mock
    private ProductoValidacionesHelper productoValidacionesHelper;

    @Mock
    private NotaVentaRepository notaVentaRepository;

    @Mock
    private NotaVentaDetalleService notaVentaDetalleService;

    @InjectMocks
    private NotaCancelacionService notaCancelacionService;

    @Test
    void crearCancelacionSuccess() {
        var detalleRegistro = new DatosRegistroCancelacionDetalle("prod-1", 5.0);
        var datos = new DatosRegistroCancelacion("orden-1", "lunes", List.of(detalleRegistro));

        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");
        orden.setActivo(true);

        Producto producto = new Producto();
        producto.setId("prod-1");
        producto.setNombre("Arroz");

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("orden-1")).willReturn(orden);
        given(productoValidacionesHelper.encontrarProductoId("prod-1")).willReturn(producto);
        given(cancelacionRepository.save(any())).willAnswer(invocation -> {
            NotaCancelacion nc = invocation.getArgument(0);
            nc.setId("cancel-1");
            return nc;
        });

        DatosListarCancelacion result = notaCancelacionService.crearCancelacion(datos, "admin");

        assertThat(result).isNotNull();
        verify(cancelacionRepository).save(any());
    }

    @Test
    void crearCancelacionOrdenInactivaThrowsResourceNotFoundException() {
        var detalleRegistro = new DatosRegistroCancelacionDetalle("prod-1", 5.0);
        var datos = new DatosRegistroCancelacion("orden-1", "lunes", List.of(detalleRegistro));

        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");
        orden.setActivo(false);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("orden-1")).willReturn(orden);

        assertThatThrownBy(() -> notaCancelacionService.crearCancelacion(datos, "admin"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Orden de compra no encontrada");
    }

    @Test
    void crearCancelacionThrowsResourceNotFoundExceptionWhenOrdenNotFound() {
        var detalleRegistro = new DatosRegistroCancelacionDetalle("prod-1", 5.0);
        var datos = new DatosRegistroCancelacion("bad-id", "lunes", List.of(detalleRegistro));

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("bad-id"))
                .willThrow(new ResourceNotFoundException("Orden de compra no encontrada"));

        assertThatThrownBy(() -> notaCancelacionService.crearCancelacion(datos, "admin"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Orden de compra no encontrada");
    }

    @Test
    void listarPorOrdenSuccess() {
        Producto producto = new Producto();
        producto.setId("prod-1");
        producto.setNombre("Arroz");

        NotaCancelacionDetalle detalle = new NotaCancelacionDetalle();
        detalle.setId("det-1");
        detalle.setProducto(producto);
        detalle.setCantidadCancelada(5.0);

        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");

        NotaCancelacion nc = new NotaCancelacion();
        nc.setId("cancel-1");
        nc.setOrdenCompra(orden);
        nc.setDia("lunes");
        nc.setFechaCreacion(LocalDateTime.now());
        nc.setCreadoPor("admin");
        nc.setDetalles(List.of(detalle));

        given(cancelacionRepository.findByOrdenCompraIdAndActivoTrue("orden-1")).willReturn(List.of(nc));

        List<DatosListarCancelacion> result = notaCancelacionService.listarPorOrden("orden-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo("cancel-1");
    }

    @Test
    void listarPorOrdenEmptyWhenNoCancelaciones() {
        given(cancelacionRepository.findByOrdenCompraIdAndActivoTrue("orden-1")).willReturn(List.of());

        List<DatosListarCancelacion> result = notaCancelacionService.listarPorOrden("orden-1");

        assertThat(result).isEmpty();
    }

    @Test
    void validarCancelacionSuccess() {
        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");

        NotaCancelacion nc = new NotaCancelacion();
        nc.setId("cancel-1");
        nc.setOrdenCompra(orden);
        nc.setDia("lunes");
        nc.setFechaCreacion(LocalDateTime.now());
        nc.setCreadoPor("admin");
        nc.setValidadoPor(null);
        nc.setDetalles(List.of());

        given(cancelacionRepository.findByIdAndActivoTrue("cancel-1")).willReturn(Optional.of(nc));
        given(cancelacionRepository.save(nc)).willReturn(nc);

        DatosListarCancelacion result = notaCancelacionService.validarCancelacion("cancel-1", "validador");

        assertThat(result).isNotNull();
        assertThat(nc.getValidadoPor()).isEqualTo("validador");
        assertThat(nc.getFechaValidacion()).isNotNull();
        verify(cancelacionRepository).save(nc);
    }

    @Test
    void validarCancelacionAlreadyValidatedThrowsRecursoExistenteException() {
        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");

        NotaCancelacion nc = new NotaCancelacion();
        nc.setId("cancel-1");
        nc.setOrdenCompra(orden);
        nc.setDia("lunes");
        nc.setValidadoPor("otro-admin");
        nc.setFechaValidacion(LocalDateTime.now());

        given(cancelacionRepository.findByIdAndActivoTrue("cancel-1")).willReturn(Optional.of(nc));

        assertThatThrownBy(() -> notaCancelacionService.validarCancelacion("cancel-1", "validador"))
                .isInstanceOf(RecursoExistenteException.class)
                .hasMessageContaining("ya fue validada");
    }

    @Test
    void validarCancelacionNotFoundThrowsResourceNotFoundException() {
        given(cancelacionRepository.findByIdAndActivoTrue("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> notaCancelacionService.validarCancelacion("bad-id", "admin"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cancelación no encontrada");
    }

    @Test
    void eliminarCancelacionSuccess() {
        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");

        NotaCancelacion nc = new NotaCancelacion();
        nc.setId("cancel-1");
        nc.setOrdenCompra(orden);
        nc.setActivo(true);

        given(cancelacionRepository.findByIdAndActivoTrue("cancel-1")).willReturn(Optional.of(nc));

        notaCancelacionService.eliminarCancelacion("cancel-1");

        assertThat(nc.getActivo()).isFalse();
        verify(cancelacionRepository).save(nc);
    }

    @Test
    void eliminarCancelacionNotFoundThrowsResourceNotFoundException() {
        given(cancelacionRepository.findByIdAndActivoTrue("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> notaCancelacionService.eliminarCancelacion("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cancelación no encontrada");
    }

    @Test
    void reconstruirNotasSuccess() {
        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");

        Producto producto = new Producto();
        producto.setId("prod-1");
        producto.setNombre("Arroz");
        producto.setPrecioVenta(BigDecimal.valueOf(20));

        OrdenCompraDetalle detalleOC = new OrdenCompraDetalle();
        detalleOC.setProducto(producto);
        detalleOC.setLunes(10.0);
        detalleOC.setMartes(0.0);
        orden.setDetalles(List.of(detalleOC));

        NotaCancelacionDetalle detalleCanc = new NotaCancelacionDetalle();
        detalleCanc.setProducto(producto);
        detalleCanc.setCantidadCancelada(3.0);

        NotaCancelacion nc = new NotaCancelacion();
        nc.setId("cancel-1");
        nc.setOrdenCompra(orden);
        nc.setDia("lunes");
        nc.setFechaCreacion(LocalDateTime.now());
        nc.setCreadoPor("admin");
        nc.setValidadoPor("validador");
        nc.setFechaValidacion(LocalDateTime.now());
        nc.setDetalles(List.of(detalleCanc));

        NotaVenta notaExistente = new NotaVenta();
        notaExistente.setId("nota-1");
        notaExistente.setDetalles(new java.util.ArrayList<>());
        notaExistente.setTotalGeneral(BigDecimal.valueOf(200));

        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        notaExistente.setCliente(cliente);
        notaExistente.setPartida(Partida.ABARROTES);
        notaExistente.setFecha(LocalDateTime.now());
        notaExistente.setActivo(true);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("orden-1")).willReturn(orden);
        given(cancelacionRepository.findByOrdenCompraIdAndActivoTrue("orden-1")).willReturn(List.of(nc));
        given(notaVentaRepository.findByOrdenCompraIdAndDiaAndActivoTrue("orden-1", "lunes"))
                .willReturn(Optional.of(notaExistente));
        given(notaVentaRepository.save(notaExistente)).willReturn(notaExistente);

        List<DatosDetalleNota> result = notaCancelacionService.reconstruirNotas("orden-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo("nota-1");
        verify(notaVentaRepository).save(notaExistente);
    }
}
