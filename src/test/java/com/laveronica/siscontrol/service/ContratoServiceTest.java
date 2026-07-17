package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosActualizarContrato;
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
        var datos = new DatosRegistroContrato("CON-001", "1", LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000), null, null);
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        given(clienteValidacionesHelper.validaClienteExistaId("1")).willReturn(cliente);
        given(contratoRepository.save(any())).willAnswer(invocation -> {
            Contrato c = invocation.getArgument(0);
            c.setId("1");
            return c;
        });

        Contrato result = contratoService.registrarContrato(datos);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getContrato()).isEqualTo("CON-001");
        assertThat(result.getCliente()).isEqualTo(cliente);
        verify(contratoRepository).save(any());
    }

    @Test
    void registrarContratoThrowsResourceNotFoundExceptionWhenClienteNotFound() {
        var datos = new DatosRegistroContrato("CON-001", "bad-id", LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000), null, null);

        given(clienteValidacionesHelper.validaClienteExistaId("bad-id"))
                .willThrow(new ResourceNotFoundException("Cliente no encontrado"));

        assertThatThrownBy(() -> contratoService.registrarContrato(datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void listarContratosReturnsList() {
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        Contrato contrato = new Contrato();
        contrato.setId("1");
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
        assertThat(result.get(0).cliente()).isEqualTo("Cliente Test");
    }

    @Test
    void listarContratosReturnsEmptyList() {
        given(contratoRepository.findAllByActivoTrue()).willReturn(List.of());

        List<DatosDetalleContrato> result = contratoService.listarContratos();

        assertThat(result).isEmpty();
    }

    @Test
    void buscarContratoIdFound() {
        var detalle = new DatosDetalleContrato("1", "CON-001", "Cliente Test", LocalDate.now(), LocalDate.now().plusDays(30), BigDecimal.valueOf(5000), null, null);

        given(contratoValidacionesHelper.buscarContratoExisteId("1")).willReturn(detalle);

        var result = contratoService.buscarContratoId("1");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
        assertThat(result.contrato()).isEqualTo("CON-001");
    }

    @Test
    void buscarContratoIdThrowsResourceNotFoundException() {
        given(contratoValidacionesHelper.buscarContratoExisteId("bad-id"))
                .willThrow(new ResourceNotFoundException("Contrato no encontrado"));

        assertThatThrownBy(() -> contratoService.buscarContratoId("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Contrato no encontrado");
    }

    @Test
    void actualizarContratoSuccess() {
        Cliente clienteOriginal = new Cliente();
        clienteOriginal.setId("1");
        clienteOriginal.setNombre("Cliente Original");

        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setId("2");
        clienteNuevo.setNombre("Cliente Nuevo");

        Contrato contrato = new Contrato();
        contrato.setId("1");
        contrato.setContrato("CON-001");
        contrato.setCliente(clienteOriginal);
        contrato.setFechaInicio(LocalDate.now());
        contrato.setFechaTermino(LocalDate.now().plusDays(30));
        contrato.setPresupuesto(BigDecimal.valueOf(5000));
        contrato.setActivo(true);

        var datos = new DatosActualizarContrato("2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(60), BigDecimal.valueOf(10000), null, null);

        given(contratoValidacionesHelper.validaContratoExisteId("1")).willReturn(contrato);
        given(clienteValidacionesHelper.validaClienteExistaId("2")).willReturn(clienteNuevo);
        given(contratoRepository.save(contrato)).willReturn(contrato);

        DatosDetalleContrato result = contratoService.actualizarContratoId("1", datos);

        assertThat(result).isNotNull();
        assertThat(contrato.getCliente()).isEqualTo(clienteNuevo);
        assertThat(contrato.getFechaInicio()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(contrato.getFechaTermino()).isEqualTo(LocalDate.now().plusDays(60));
        assertThat(contrato.getPresupuesto()).isEqualTo(BigDecimal.valueOf(10000));
        verify(contratoRepository).save(contrato);
    }

    @Test
    void actualizarContratoThrowsResourceNotFoundException() {
        var datos = new DatosActualizarContrato(null, null, null, null, null, null);

        given(contratoValidacionesHelper.validaContratoExisteId("bad-id"))
                .willThrow(new ResourceNotFoundException("Contrato no encontrado"));

        assertThatThrownBy(() -> contratoService.actualizarContratoId("bad-id", datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Contrato no encontrado");
    }

    @Test
    void eliminarContratoSuccess() {
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");

        Contrato contrato = new Contrato();
        contrato.setId("1");
        contrato.setContrato("CON-001");
        contrato.setCliente(cliente);
        contrato.setActivo(true);

        given(contratoValidacionesHelper.validaContratoExisteId("1")).willReturn(contrato);

        contratoService.eliminarContrato("1");

        assertThat(contrato.getActivo()).isFalse();
        verify(contratoValidacionesHelper).validaContratoExisteId("1");
    }

    @Test
    void eliminarContratoThrowsResourceNotFoundException() {
        given(contratoValidacionesHelper.validaContratoExisteId("bad-id"))
                .willThrow(new ResourceNotFoundException("Contrato no encontrado"));

        assertThatThrownBy(() -> contratoService.eliminarContrato("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Contrato no encontrado");
    }
}
