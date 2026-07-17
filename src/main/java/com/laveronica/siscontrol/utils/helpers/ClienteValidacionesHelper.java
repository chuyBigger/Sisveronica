package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.repositories.ClienteRepository;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ClienteValidacionesHelper {

    private final ClienteRepository clienteRepository;

    public Cliente validaClienteExista(String cliente){
        if (cliente == null || cliente.isBlank()){
            throw new ResourceNotFoundException("El Cliente es requerido y no puede estar vacío.");
        }
        String clienteNormalizado = cliente.toLowerCase().trim();
        Cliente clienteEncontrado;

        try {
            clienteEncontrado = clienteRepository.findByNombre(clienteNormalizado);
        } catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("El cliente '" + clienteNormalizado + "' no esta registrado o esta mal escrito.");
        }
        return clienteEncontrado;
    }

    public Cliente validaClienteExistaId(String id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("⚠️ Cliente no encontrado...")
        );
        return cliente;
    }

}
