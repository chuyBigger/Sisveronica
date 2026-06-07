package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosGenerarNotaDesdeOrden;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosActulizarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.FacturaRepository;
import com.laveronica.siscontrol.repositories.NotaCancelacionRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.services.NotaVentaService;
import com.laveronica.siscontrol.services.OrdenCompraDetalleService;
import com.laveronica.siscontrol.services.OrdenCompraService;
import com.laveronica.siscontrol.utils.helpers.ClienteValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ContratoValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.OrdenCompraValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrdenCompraServiceTest {

    @Mock
    private OrdenCompraValidacionesHelper ordenCompraValidacionesHelper;

    @Mock
    private ClienteValidacionesHelper clienteValidacionesHelper;

    @Mock
    private ContratoValidacionesHelper contratoValidacionesHelper;

    @Mock
    private OrdenCompraDetalleService ordenCompraDetalleService;

    @Mock
    private OrdenCompraRespository ordenCompraRespository;

    @Mock
    private PartidaValidacionesHelper partidaValidacionesHelper;

    @Mock
    private NotaVentaRepository notaVentaRepository;

    @Mock
    private NotaVentaService notaVentaService;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private NotaCancelacionRepository notaCancelacionRepository;

    @InjectMocks
    private OrdenCompraService ordenCompraService;

    @Test
    void registrarOrdenCompraSuccess() {
        var detalleRegistro = new DatosRegistroOrdenCompraDetalle(LocalDate.now(), "1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var datos = new DatosRegistroOrdenCompra("1", "1", "ABARROTES", LocalDate.now(), List.of(detalleRegistro));

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        Contrato contrato = new Contrato();
        contrato.setId("1");
        contrato.setContrato("CON-001");

        given(partidaValidacionesHelper.validaPartidaExistaString("ABARROTES")).willReturn(Partida.ABARROTES);
        given(clienteValidacionesHelper.validaClienteExistaId("1")).willReturn(cliente);
        given(contratoValidacionesHelper.validaContratoExisteId("1")).willReturn(contrato);
        given(ordenCompraDetalleService.registrarListaDetallesOrdenCompra(any(), any())).willReturn(List.of());
        given(ordenCompraRespository.save(any())).willAnswer(invocation -> {
            OrdenCompra oc = invocation.getArgument(0);
            oc.setId("1");
            return oc;
        });

        DatosDetalleOrdenCompra result = ordenCompraService.registrarOrdenCompra(datos);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
        verify(ordenCompraRespository).save(any());
    }

    @Test
    void listarOrdenesCompraReturnsPage() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");
        oc.setPartida(Partida.ABARROTES);
        oc.setFechaInicioSemana(LocalDate.now());
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        oc.setCliente(cliente);
        Contrato contrato = new Contrato();
        contrato.setContrato("CON-001");
        oc.setContrato(contrato);

        Page<OrdenCompra> page = new PageImpl<>(List.of(oc), PageRequest.of(0, 9), 1);

        given(ordenCompraRespository.findByAndActivoTrue(any())).willReturn(page);
        given(facturaRepository.findOrdenCompraIdsWithFactura(any())).willReturn(Set.of());
        given(notaVentaRepository.findNotaCountsByOrdenCompraIds(any())).willReturn(List.of());
        given(notaCancelacionRepository.findCancelacionCountsByOrdenCompraIds(any())).willReturn(List.of());

        Page<DatosListarOrdenCompra> result = ordenCompraService.listarOrdenesCompra(PageRequest.of(0, 9));

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void buscarOrdenCompraIdFound() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");
        oc.setPartida(Partida.ABARROTES);
        oc.setFechaInicioSemana(LocalDate.now());
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        oc.setCliente(cliente);
        Contrato contrato = new Contrato();
        contrato.setContrato("CON-001");
        oc.setContrato(contrato);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("1")).willReturn(oc);

        DatosDetalleOrdenCompra result = ordenCompraService.buscarOrdenCompraId("1");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
    }

    @Test
    void buscarOrdenCompraIdThrowsResourceNotFoundException() {
        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("bad-id"))
                .willThrow(new ResourceNotFoundException("Orden de compra no encontrada"));

        assertThatThrownBy(() -> ordenCompraService.buscarOrdenCompraId("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Orden de compra no encontrada");
    }

    @Test
    void actulizarOrdenCompraIdSuccess() {
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        Contrato contrato = new Contrato();
        contrato.setId("1");
        contrato.setContrato("CON-001");

        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");
        oc.setCliente(cliente);
        oc.setContrato(contrato);
        oc.setPartida(Partida.ABARROTES);
        oc.setFechaInicioSemana(LocalDate.now());
        oc.setDetalles(new java.util.ArrayList<>());

        var datos = new DatosActulizarOrdenCompra("1", "1", "CARNES", LocalDate.now().plusDays(7), null);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("1")).willReturn(oc);
        given(partidaValidacionesHelper.validaPartidaExistaString("CARNES")).willReturn(Partida.CARNES);
        given(clienteValidacionesHelper.validaClienteExistaId("1")).willReturn(cliente);
        given(contratoValidacionesHelper.validaContratoExisteId("1")).willReturn(contrato);

        DatosDetalleOrdenCompra result = ordenCompraService.actulizarOrdenCompraId("1", datos);

        assertThat(result).isNotNull();
        assertThat(oc.getPartida()).isEqualTo(Partida.CARNES);
    }

    @Test
    void actulizarOrdenCompraIdThrowsResourceNotFoundException() {
        var datos = new DatosActulizarOrdenCompra(null, null, null, null, null);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("bad-id"))
                .willThrow(new ResourceNotFoundException("Orden de compra no encontrada"));

        assertThatThrownBy(() -> ordenCompraService.actulizarOrdenCompraId("bad-id", datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Orden de compra no encontrada");
    }

    @Test
    void eliminarOrdenCompraSuccess() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");
        oc.setActivo(true);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("1")).willReturn(oc);

        ordenCompraService.eliminarOrdenCompra("1");

        assertThat(oc.getActivo()).isFalse();
        verify(ordenCompraValidacionesHelper).buscarOrdenCompraId("1");
    }

    @Test
    void eliminarOrdenCompraThrowsResourceNotFoundException() {
        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("bad-id"))
                .willThrow(new ResourceNotFoundException("Orden de compra no encontrada"));

        assertThatThrownBy(() -> ordenCompraService.eliminarOrdenCompra("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Orden de compra no encontrada");
    }

    @Test
    void confirmarOrdenCompraSuccess() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");
        oc.setPartida(Partida.ABARROTES);
        oc.setFechaInicioSemana(LocalDate.now());
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        oc.setCliente(cliente);
        Contrato contrato = new Contrato();
        contrato.setContrato("CON-001");
        oc.setContrato(contrato);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("1")).willReturn(oc);
        given(ordenCompraRespository.save(oc)).willReturn(oc);

        DatosDetalleOrdenCompra result = ordenCompraService.confirmarOrdenCompra("1", "admin");

        assertThat(result).isNotNull();
        assertThat(oc.getConfirmadoPor()).isEqualTo("admin");
        assertThat(oc.getFechaConfirmacion()).isNotNull();
        verify(ordenCompraRespository).save(oc);
    }

    @Test
    void listarNotasPorOrdenSuccess() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");

        NotaVenta nota1 = new NotaVenta();
        nota1.setId("n1");
        nota1.setCliente(cliente);
        nota1.setPartida(Partida.ABARROTES);
        nota1.setFecha(LocalDateTime.now());
        nota1.setTotalGeneral(BigDecimal.valueOf(100));

        NotaVenta nota2 = new NotaVenta();
        nota2.setId("n2");
        nota2.setCliente(cliente);
        nota2.setPartida(Partida.ABARROTES);
        nota2.setFecha(LocalDateTime.now());
        nota2.setTotalGeneral(BigDecimal.valueOf(200));

        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("1")).willReturn(oc);
        given(notaVentaRepository.findByOrdenCompraIdAndActivoTrue("1")).willReturn(List.of(nota1, nota2));

        List<DatosListarNota> result = ordenCompraService.listarNotasPorOrden("1");

        assertThat(result).hasSize(2);
    }

    @Test
    void generarTodasNotasSuccess() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("1")).willReturn(oc);

        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        NotaVenta nota = new NotaVenta();
        nota.setId("n1");
        nota.setCliente(cliente);
        nota.setPartida(Partida.ABARROTES);
        nota.setFecha(LocalDateTime.now());
        nota.setTotalGeneral(BigDecimal.valueOf(100));

        var detalleNota = new DatosDetalleNota(nota);
        for (String dia : new String[]{"lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"}) {
            given(notaVentaService.generarNotaDesdeOrden(new DatosGenerarNotaDesdeOrden("1", dia)))
                    .willReturn(detalleNota);
        }

        List<DatosDetalleNota> result = ordenCompraService.generarTodasNotas("1");

        assertThat(result).hasSize(7);
    }

    @Test
    void generarTodasNotasIgnoresExceptions() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId("1");

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId("1")).willReturn(oc);

        given(notaVentaService.generarNotaDesdeOrden(new DatosGenerarNotaDesdeOrden("1", "lunes")))
                .willThrow(new ResourceNotFoundException("No hay productos"));

        for (String dia : new String[]{"martes", "miercoles", "jueves", "viernes", "sabado", "domingo"}) {
            given(notaVentaService.generarNotaDesdeOrden(new DatosGenerarNotaDesdeOrden("1", dia)))
                    .willThrow(new ResourceNotFoundException("No hay productos"));
        }

        List<DatosDetalleNota> result = ordenCompraService.generarTodasNotas("1");

        assertThat(result).isEmpty();
    }
}
