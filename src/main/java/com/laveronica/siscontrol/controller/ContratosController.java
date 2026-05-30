package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.contratos.*;
import com.laveronica.siscontrol.domain.contratos.dto.DatosActualizarContrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosDetalleContrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosRegistroContrato;
import com.laveronica.siscontrol.services.ContratoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/contratos")
public class ContratosController {

    @Autowired
    private ContratoService contratoService;

    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroContrato datos, UriComponentsBuilder uriComponentsBuilder) {
        Contrato nuevoContrato = contratoService.registrarContrato(datos);
        var uri = uriComponentsBuilder.path("/contratos/{id}").buildAndExpand(nuevoContrato.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetalleContrato(nuevoContrato));
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<DatosDetalleContrato>> contratosLista() {
        List<DatosDetalleContrato> lista = contratoService.listarContratos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleContrato> contratoBuscarId(@PathVariable String id) {
        DatosDetalleContrato contratoId = contratoService.buscarContratoId(id);
        return ResponseEntity.ok(contratoId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DatosDetalleContrato> actualizarContrato(@PathVariable String id, @Valid @RequestBody DatosActualizarContrato datos){
        DatosDetalleContrato actualizarContrato = contratoService.actualizarContratoId(id, datos);
        return ResponseEntity.ok(actualizarContrato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarContrato(@PathVariable String id){
         contratoService.eliminarContrato(id);
    }

}
