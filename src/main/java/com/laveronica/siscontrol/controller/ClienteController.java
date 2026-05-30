package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.clientes.*;
import com.laveronica.siscontrol.domain.clientes.dto.DatosActualizarCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosDetalleCliente;
import com.laveronica.siscontrol.domain.clientes.dto.DatosRegistroCliente;
import com.laveronica.siscontrol.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/clientes")

public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroCliente datos, UriComponentsBuilder uriComponentsBuilder) {
        Cliente nuevoCliente = clienteService.registarCliente(datos);
        var uri = uriComponentsBuilder.path("/clientes/{id}").buildAndExpand(nuevoCliente.getId()).toUri();
        return ResponseEntity.created(uri).body("✅ Usuario registrado Exitosamente:");
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<DatosDetalleCliente>> listarClietes() {
        List<DatosDetalleCliente> clientes = clienteService.buscarTodos();
        return ResponseEntity.ok(clientes);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleCliente> mostrarClienteId(@PathVariable String id) {
        DatosDetalleCliente cliente = clienteService.buscarClienteId(id);
        return ResponseEntity.ok(cliente);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DatosDetalleCliente> actulizarCliente(@PathVariable String id, @RequestBody DatosActualizarCliente datos) {
        DatosDetalleCliente cliente = clienteService.actualizarCliente(id, datos);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }


}
