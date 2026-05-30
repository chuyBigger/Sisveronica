package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosDetalleContrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosRegistroContrato;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.ClienteRepository;
import com.laveronica.siscontrol.repositories.ContratoRepository;
import com.laveronica.siscontrol.services.ContratoService;
import com.laveronica.siscontrol.utils.helpers.ClienteValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ContratoValidacionesHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContratoServiceTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteValidacionesHelper clienteValidacionesHelper;

    @Mock
    private ContratoValidacionesHelper contratoValidacionesHelper;

    @InjectMocks
    private ContratoService contratoService;

    @Test
    void registrarContratoSuccess() {
        var datos = new DatosRegistroContrato("CON-001", 1L, LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000));
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Cliente Test");

        given(clienteValidacionesHelper.validaClienteExistaId(1L)).willReturn(cliente);
        given(contratoRepository.save(any())).willAnswer(invocation -> {
            Contrato c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Contrato result = contratoService.registrarContrato(datos);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void listarContratosReturnsList() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Cliente Test");

        Contrato contrato = new Contrato();
        contrato.setId(1L);
        contrato.setContrato("CON-001");
        contrato.setCliente(cliente);
        contrato.setFechaInicio(LocalDate.now());
        contrato.setFechaTermino(LocalDate.now().plusDays(30));
        contrato.setPresupuesto(BigDecimal.valueOf(5000));
        contrato.setActivo(true);

        given(contratoRepository.findAllByActivoTrue()).willReturn(List.of(contrato));

        List<DatosDetalleContrato> result = contratoService.listarContratos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).contrato()).isEqualTo("CON-001");
    }

    @Test
    void buscarContratoIdFound() {
        var detalle = new DatosDetalleContrato(1L, "CON-001", "Cliente Test", LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000));

        given(contratoValidacionesHelper.buscarContratoExisteId(1L)).willReturn(detalle);

        var result = contratoService.buscarContratoId(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void eliminarContratoSetsActivoFalse() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Cliente Test");

        Contrato contrato = new Contrato();
        contrato.setId(1L);
        contrato.setContrato("CON-001");
        contrato.setCliente(cliente);
        contrato.setActivo(true);

        given(contratoValidacionesHelper.validaContratoExisteId(1L)).willReturn(contrato);

        contratoService.eliminarContrato(1L);

        assertThat(contrato.getActivo()).isFalse();
        verify(contratoValidacionesHelper).validaContratoExisteId(1L);
    }
}
