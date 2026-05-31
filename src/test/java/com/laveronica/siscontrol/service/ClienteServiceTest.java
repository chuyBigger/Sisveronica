package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosDetalleCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosRegistroCliente;
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
}
