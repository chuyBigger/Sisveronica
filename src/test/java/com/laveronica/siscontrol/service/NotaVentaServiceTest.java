package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosRegistroNota;
import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaDetalleRegistro;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaListarDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NotaVentaServiceTest {

    @Mock
    private NotaVentaRepository notaVentaRepository;

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

    @InjectMocks
    private NotaVentaService notaVentaService;

    @Test
    void registrarNotaSuccess() {
        var detalleRegistro = new NotaVentaDetalleRegistro(5, "1");
        var datos = new DatosRegistroNota(1L, "ABARROTES", List.of(detalleRegistro));

        Cliente cliente = new Cliente();
        cliente.setId(1L);
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

        given(clienteValidacionesHelper.validaClienteExistaId(1L)).willReturn(cliente);
        given(partidaValidacionesHelper.validaPartidaExistaString("ABARROTES")).willReturn(Partida.ABARROTES);
        given(notaVentaDetalleService.registrarNuevaListaNotaVentasDetalles(any(), any())).willReturn(List.of(detalle));
        given(notaVentaRepository.save(any())).willAnswer(invocation -> {
            NotaVenta nv = invocation.getArgument(0);
            nv.setId(1L);
            return nv;
        });

        DatosDetalleNota result = notaVentaService.registrarNota(datos);

        assertThat(result).isNotNull();
    }

    @Test
    void listarNotasReturnsPage() {
        NotaVenta nota = new NotaVenta();
        nota.setId(1L);
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
    }

    @Test
    void buscarNotaIdFound() {
        NotaVenta nota = new NotaVenta();
        nota.setId(1L);
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        nota.setCliente(cliente);
        nota.setPartida(Partida.ABARROTES);
        nota.setFecha(LocalDateTime.now());
        nota.setTotalGeneral(BigDecimal.valueOf(100));

        given(notaVentaValidacionesHelper.notaVentaExiste(1L)).willReturn(nota);

        DatosDetalleNota result = notaVentaService.buscarNotaId(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }
}
