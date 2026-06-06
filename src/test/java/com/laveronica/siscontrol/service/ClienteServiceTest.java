package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosActualizarCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosDetalleCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosRegistroCliente;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.ClienteRepository;
import com.laveronica.siscontrol.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void registarClienteSuccess() {
        var datos = new DatosRegistroCliente("Cliente Test", "XAXX010101000", "Calle 1", 123, "Fracc", "12345", "Municipio", "Estado");

        given(clienteRepository.existsByNombre("Cliente Test")).willReturn(false);
        given(clienteRepository.save(any())).willAnswer(invocation -> {
            Cliente c = invocation.getArgument(0);
            c.setId("1");
            return c;
        });

        Cliente result = clienteService.registarCliente(datos);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getNombre()).isEqualTo("Cliente Test");
        verify(clienteRepository).save(any());
    }

    @Test
    void registarClienteThrowsRecursoExistenteException() {
        var datos = new DatosRegistroCliente("Cliente Test", "XAXX010101000", "Calle 1", 123, "Fracc", "12345", "Municipio", "Estado");

        given(clienteRepository.existsByNombre("Cliente Test")).willReturn(true);

        assertThatThrownBy(() -> clienteService.registarCliente(datos))
                .isInstanceOf(RecursoExistenteException.class)
                .hasMessageContaining("ya existe");
    }

    @Test
    void buscarTodosReturnsActiveClients() {
        Cliente c1 = new Cliente();
        c1.setId("1");
        c1.setNombre("Cliente Activo");
        c1.setActivo(true);

        Cliente c2 = new Cliente();
        c2.setId("2");
        c2.setNombre("Cliente Inactivo");
        c2.setActivo(false);

        given(clienteRepository.findAll()).willReturn(List.of(c1, c2));

        List<DatosDetalleCliente> result = clienteService.buscarTodos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nombre()).isEqualTo("Cliente Activo");
    }

    @Test
    void buscarClienteIdFound() {
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Cliente Test");
        cliente.setRfc("XAXX010101000");
        cliente.setCalle("Calle 1");
        cliente.setNumero(123);
        cliente.setFraccionamiento("Fracc");
        cliente.setCp("12345");
        cliente.setMunicipio("Municipio");
        cliente.setEstado("Estado");
        cliente.setActivo(true);

        given(clienteRepository.findById("1")).willReturn(Optional.of(cliente));

        DatosDetalleCliente result = clienteService.buscarClienteId("1");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
        assertThat(result.nombre()).isEqualTo("Cliente Test");
    }

    @Test
    void buscarClienteIdThrowsResourceNotFoundException() {
        given(clienteRepository.findById("99")).willReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarClienteId("99"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no encontrado con ID");
    }

    @Test
    void actualizarClienteSuccess() {
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Viejo Nombre");
        cliente.setCalle("Calle Vieja");
        cliente.setActivo(true);

        var datos = new DatosActualizarCliente("Nuevo Nombre", "Nueva Calle", 456, "Nuevo Fracc", "67890", "Nuevo Mun", "Nuevo Edo");

        given(clienteRepository.findById("1")).willReturn(Optional.of(cliente));
        given(clienteRepository.save(cliente)).willReturn(cliente);

        DatosDetalleCliente result = clienteService.actualizarCliente("1", datos);

        assertThat(result).isNotNull();
        assertThat(cliente.getNombre()).isEqualTo("Nuevo Nombre");
        assertThat(cliente.getCalle()).isEqualTo("Nueva Calle");
        assertThat(cliente.getNumero()).isEqualTo(456);
        assertThat(cliente.getFraccionamiento()).isEqualTo("Nuevo Fracc");
        assertThat(cliente.getCp()).isEqualTo("67890");
        assertThat(cliente.getMunicipio()).isEqualTo("Nuevo Mun");
        assertThat(cliente.getEstado()).isEqualTo("Nuevo Edo");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void actualizarClienteThrowsResourceNotFoundException() {
        var datos = new DatosActualizarCliente("Nuevo", null, null, null, null, null, null);

        given(clienteRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.actualizarCliente("bad-id", datos))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void eliminarClienteSuccess() {
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setActivo(true);

        given(clienteRepository.findById("1")).willReturn(Optional.of(cliente));

        clienteService.eliminarCliente("1");

        assertThat(cliente.isActivo()).isFalse();
        verify(clienteRepository).findById("1");
    }

    @Test
    void eliminarClienteThrowsResourceNotFoundException() {
        given(clienteRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.eliminarCliente("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no existe");
    }

    @Test
    void eliminarClienteAlreadyInactiveThrowsResourceNotFoundException() {
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setActivo(false);

        given(clienteRepository.findById("1")).willReturn(Optional.of(cliente));

        assertThatThrownBy(() -> clienteService.eliminarCliente("1"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ya se encuentra eliminado");
    }
}
