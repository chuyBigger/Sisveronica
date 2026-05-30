package com.laveronica.siscontrol.services;


import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.repositories.ClienteRepository;
import com.laveronica.siscontrol.domain.clientes.dto.DatosActualizarCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosDetalleCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosRegistroCliente;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    @Transactional
    public Cliente registarCliente(DatosRegistroCliente datos) {
        if (!clienteRepository.existsByNombre(datos.nombre())) {
            Cliente nuevoCliente = new Cliente(datos);
            clienteRepository.save(nuevoCliente);
            return nuevoCliente;
        } else {
            throw new RecursoExistenteException("⚠️ Error el registro ya existe. ");
        }
    }

    public List<DatosDetalleCliente> buscarTodos() {
        return clienteRepository.findAll().stream().filter(Cliente::isActivo).map(DatosDetalleCliente::new).toList();
    }

    public DatosDetalleCliente buscarClienteId(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("⚠️ Cliente no encontrado con ID: " + id));
        return new DatosDetalleCliente(cliente);
    }

    @Transactional
    public DatosDetalleCliente actualizarCliente(Long id, DatosActualizarCliente datos) {
        Cliente actualizarCliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(" ⚠️ Cliente no encontrado"));

        if (datos.nombre() != null) actualizarCliente.setNombre(datos.nombre());
        if (datos.calle() != null) actualizarCliente.setCalle(datos.calle());
        if (datos.numero() != null) actualizarCliente.setNumero(datos.numero());
        if (datos.fraccionamiento() != null) actualizarCliente.setFraccionamiento(datos.fraccionamiento());
        if (datos.cp() != null) actualizarCliente.setCp(datos.cp());
        if (datos.municipio() != null) actualizarCliente.setMunicipio(datos.municipio());
        if (datos.estado() != null) actualizarCliente.setEstado(datos.estado());

        clienteRepository.save(actualizarCliente);
        return new DatosDetalleCliente(actualizarCliente);
    }

    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("⚠️ el cliente que intenta eliminar no existe"));
        if (!cliente.isActivo()) {
            throw new ResourceNotFoundException("⛔ el cliente ya se encuentra eliminado");
        }
        cliente.setActivo(false);
    }

}
