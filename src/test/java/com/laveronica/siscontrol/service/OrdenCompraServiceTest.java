package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    @InjectMocks
    private OrdenCompraService ordenCompraService;

    @Test
    void registrarOrdenCompraSuccess() {
        var detalleRegistro = new DatosRegistroOrdenCompraDetalle(LocalDate.now(), "1", 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 0.0);
        var datos = new DatosRegistroOrdenCompra(1L, 1L, "ABARROTES", LocalDate.now(), List.of(detalleRegistro));

        given(partidaValidacionesHelper.validaPartidaExistaString("ABARROTES")).willReturn(Partida.ABARROTES);
        given(clienteValidacionesHelper.validaClienteExistaId(1L)).willReturn(new Cliente());
        given(contratoValidacionesHelper.validaContratoExisteId(1L)).willReturn(new Contrato());
        given(ordenCompraDetalleService.registrarListaDetallesOrdenCompra(any(), any())).willReturn(List.of());
        given(ordenCompraRespository.save(any())).willAnswer(invocation -> {
            OrdenCompra oc = invocation.getArgument(0);
            oc.setId(1L);
            return oc;
        });

        DatosDetalleOrdenCompra result = ordenCompraService.registrarOrdenCompra(datos);

        assertThat(result).isNotNull();
    }

    @Test
    void listarOrdenesCompraReturnsPage() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId(1L);
        oc.setPartida(Partida.ABARROTES);
        oc.setFechaInicioSemana(LocalDate.now());
        oc.setCliente(new Cliente());
        oc.setContrato(new Contrato());

        Page<OrdenCompra> page = new PageImpl<>(List.of(oc), PageRequest.of(0, 9), 1);

        given(ordenCompraRespository.findByAndActivoTrue(any())).willReturn(page);

        Page<DatosListarOrdenCompra> result = ordenCompraService.listarOrdenesCompra(PageRequest.of(0, 9));

        assertThat(result).isNotEmpty();
    }

    @Test
    void buscarOrdenCompraIdFound() {
        OrdenCompra oc = new OrdenCompra();
        oc.setId(1L);
        oc.setPartida(Partida.ABARROTES);
        oc.setFechaInicioSemana(LocalDate.now());
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Test");
        oc.setCliente(cliente);
        Contrato contrato = new Contrato();
        contrato.setContrato("CON-001");
        oc.setContrato(contrato);

        given(ordenCompraValidacionesHelper.buscarOrdenCompraId(1L)).willReturn(oc);

        DatosDetalleOrdenCompra result = ordenCompraService.buscarOrdenCompraId(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }
}
