package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosActualizarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosGenerarNotaDesdeOrden;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosRegistroNota;
import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaActualizarDetalle;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaDetalleRegistro;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaListarDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.FacturaRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.services.NotaVentaDetalleService;
import com.laveronica.siscontrol.services.NotaVentaService;
import com.laveronica.siscontrol.utils.helpers.ClienteValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.NotaVentaValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
class NotaVentaServiceTest {

    @Mock
    private NotaVentaRepository notaVentaRepository;

    @Mock
    private OrdenCompraRespository ordenCompraRespository;

    @Mock
    private ClienteValidacionesHelper clienteValidacionesHelper;

    @Mock
    private PartidaValidacionesHelper partidaValidacionesHelper;

    @Mock
    private ProductoValidacionesHelper productoValidacionesHelper;

    @Mock
    private NotaVentaDetalleService notaVentaDetalleService;

    @Mock
    private NotaVentaValidacionesHelper notaVentaValidacionesHelper;

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private NotaVentaService notaVentaService;

    @Test
    void registrarNotaSuccess() {
        var detalleRegistro = new NotaVentaDetalleRegistro(5, "1");
        var datos = new DatosRegistroNota("1", "ABARROTES", List.of(detalleRegistro));

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("arroz");
        producto.setPrecioVenta(BigDecimal.valueOf(20));
        NotaVentaDetalle detalle = new NotaVentaDetalle();
        detalle.setCantidad(5);
        detalle.setProducto(producto);
        detalle.setPrecioVenta(BigDecimal.valueOf(20));
        detalle.setSubTotal(BigDecimal.valueOf(100));

        given(clienteValidacionesHelper.validaClienteExistaId("1")).willReturn(cliente);
        given(partidaValidacionesHelper.validaPartidaExistaString("ABARROTES")).willReturn(Partida.ABARROTES);
        given(notaVentaDetalleService.registrarNuevaListaNotaVentasDetalles(any(), any())).willReturn(List.of(detalle));
        given(notaVentaRepository.save(any())).willAnswer(invocation -> {
            NotaVenta nv = invocation.getArgument(0);
            nv.setId("1");
            return nv;
        });

        DatosDetalleNota result = notaVentaService.registrarNota(datos);

        assertThat(result).isNotNull();
        verify(notaVentaRepository).save(any());
    }

    @Test
    void listarNotasReturnsPage() {
        NotaVenta nota = new NotaVenta();
        nota.setId("1");
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        nota.setCliente(cliente);
        nota.setPartida(Partida.ABARROTES);
        nota.setFecha(LocalDateTime.now());
        nota.setTotalGeneral(BigDecimal.valueOf(100));

        Page<NotaVenta> page = new PageImpl<>(List.of(nota), PageRequest.of(0, 9), 1);

        given(notaVentaRepository.findAllByActivoTrue(any())).willReturn(page);

        Page<DatosListarNota> result = notaVentaService.listarNotas(PageRequest.of(0, 9));

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void buscarNotaIdFound() {
        NotaVenta nota = new NotaVenta();
        nota.setId("1");
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        nota.setCliente(cliente);
        nota.setPartida(Partida.ABARROTES);
        nota.setFecha(LocalDateTime.now());
        nota.setTotalGeneral(BigDecimal.valueOf(100));

        given(notaVentaValidacionesHelper.notaVentaExiste("1")).willReturn(nota);

        DatosDetalleNota result = notaVentaService.buscarNotaId("1");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
    }

    @Test
    void buscarNotaIdThrowsResourceNotFoundException() {
        given(notaVentaValidacionesHelper.notaVentaExiste("bad-id"))
                .willThrow(new ResourceNotFoundException("El id no coicide con ninguna nota"));

        assertThatThrownBy(() -> notaVentaService.buscarNotaId("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no coicide");
    }

    @Test
    void actualizarNotaSuccess() {
        NotaVenta nota = new NotaVenta();
        nota.setId("1");
        nota.setPartida(Partida.ABARROTES);
        nota.setDetalles(new java.util.ArrayList<>());

        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("arroz");
        producto.setPrecioVenta(BigDecimal.valueOf(20));

        var detalleActualizar = new NotaVentaActualizarDetalle(10, "1");
        var datos = new DatosActualizarNota("CARNES", List.of(detalleActualizar));

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");
        nota.setCliente(cliente);

        given(notaVentaValidacionesHelper.notaVentaExiste("1")).willReturn(nota);
        given(partidaValidacionesHelper.validaPartidaExistaString("CARNES")).willReturn(Partida.CARNES);
        given(productoValidacionesHelper.encontrarProductoId("1")).willReturn(producto);

        DatosDetalleNota result = notaVentaService.actualizarNota("1", datos);

        assertThat(result).isNotNull();
        assertThat(nota.getPartida()).isEqualTo(Partida.CARNES);
        verify(notaVentaRepository).save(nota);
    }

    @Test
    void eliminarNotaSuccess() {
        NotaVenta nota = new NotaVenta();
        nota.setId("1");
        nota.setActivo(true);

        given(notaVentaValidacionesHelper.notaVentaExiste("1")).willReturn(nota);

        notaVentaService.eliminarNota("1");

        assertThat(nota.getActivo()).isFalse();
        verify(notaVentaValidacionesHelper).notaVentaExiste("1");
    }

    @Test
    void eliminarNotaThrowsResourceNotFoundException() {
        given(notaVentaValidacionesHelper.notaVentaExiste("bad-id"))
                .willThrow(new ResourceNotFoundException("El id no coicide con ninguna nota"));

        assertThatThrownBy(() -> notaVentaService.eliminarNota("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no coicide");
    }

    @Test
    void generarNotaDesdeOrdenSuccess() {
        var datos = new DatosGenerarNotaDesdeOrden("orden-1", "lunes");

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        Contrato contrato = new Contrato();
        contrato.setId("1");
        contrato.setContrato("CON-001");

        Producto producto = new Producto();
        producto.setId("prod-1");
        producto.setNombre("arroz");
        producto.setPrecioVenta(BigDecimal.valueOf(20));

        OrdenCompraDetalle detalleOC = new OrdenCompraDetalle();
        detalleOC.setProducto(producto);
        detalleOC.setLunes(5.0);
        detalleOC.setMartes(0.0);

        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");
        orden.setCliente(cliente);
        orden.setContrato(contrato);
        orden.setPartida(Partida.ABARROTES);
        orden.setDetalles(List.of(detalleOC));
        orden.setFechaInicioSemana(java.time.LocalDate.of(2026, 6, 1));

        given(ordenCompraRespository.findByIdAndActivoTrueWithLock("orden-1")).willReturn(Optional.of(orden));
        given(notaVentaRepository.findMaxFolio()).willReturn(0);
        given(notaVentaRepository.save(any())).willAnswer(invocation -> {
            NotaVenta nv = invocation.getArgument(0);
            nv.setId("nota-1");
            return nv;
        });

        DatosDetalleNota result = notaVentaService.generarNotaDesdeOrden(datos);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("nota-1");
        verify(notaVentaRepository).save(any());
    }

    @Test
    void generarNotaDesdeOrdenInvalidDayThrowsResourceNotFoundException() {
        var datos = new DatosGenerarNotaDesdeOrden("orden-1", "invalid-day");

        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");
        orden.setDetalles(List.of());

        given(ordenCompraRespository.findByIdAndActivoTrueWithLock("orden-1")).willReturn(Optional.of(orden));

        assertThatThrownBy(() -> notaVentaService.generarNotaDesdeOrden(datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Día no válido");
    }

    @Test
    void generarNotaDesdeOrdenNoProductsForDayThrowsResourceNotFoundException() {
        var datos = new DatosGenerarNotaDesdeOrden("orden-1", "domingo");

        Producto producto = new Producto();
        producto.setId("prod-1");
        producto.setNombre("arroz");
        producto.setPrecioVenta(BigDecimal.valueOf(20));

        OrdenCompraDetalle detalleOC = new OrdenCompraDetalle();
        detalleOC.setProducto(producto);
        detalleOC.setLunes(5.0);
        detalleOC.setDomingo(0.0);

        OrdenCompra orden = new OrdenCompra();
        orden.setId("orden-1");
        orden.setDetalles(List.of(detalleOC));

        given(ordenCompraRespository.findByIdAndActivoTrueWithLock("orden-1")).willReturn(Optional.of(orden));

        assertThatThrownBy(() -> notaVentaService.generarNotaDesdeOrden(datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No hay productos con cantidad para el día");
    }

    @Test
    void generarNotaDesdeOrdenOrdenNotFoundThrowsResourceNotFoundException() {
        var datos = new DatosGenerarNotaDesdeOrden("bad-id", "lunes");

        given(ordenCompraRespository.findByIdAndActivoTrueWithLock("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> notaVentaService.generarNotaDesdeOrden(datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Orden de compra no encontrada");
    }
}
