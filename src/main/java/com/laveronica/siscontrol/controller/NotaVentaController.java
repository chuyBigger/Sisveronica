package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.notaventa.dto.DatosActualizarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosGenerarNotaDesdeOrden;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.services.NotaVentaService;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosRegistroNota;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/notaventas")
@Tag(name = "Notas de Venta")
@SecurityRequirement(name = "bearerAuth")
public class NotaVentaController {

    @Autowired
    private NotaVentaService notaVentaService;

    @PostMapping
    @Operation(summary = "Registrar nota de venta")
    public ResponseEntity<DatosDetalleNota> registrarNota(@RequestBody @Valid DatosRegistroNota datos, UriComponentsBuilder uriComponentsBuilder) {
        DatosDetalleNota nuevaNotaVenta = notaVentaService.registrarNota(datos);
        var uri = uriComponentsBuilder.path("/notaventas/{id}").buildAndExpand(nuevaNotaVenta.id()).toUri();
        return ResponseEntity.created(uri).body(nuevaNotaVenta);
    }

    @PostMapping("/generar-desde-orden")
    @Operation(summary = "Generar nota desde orden")
    public ResponseEntity<DatosDetalleNota> generarNotaDesdeOrden(@RequestBody @Valid DatosGenerarNotaDesdeOrden datos) {
        DatosDetalleNota nuevaNota = notaVentaService.generarNotaDesdeOrden(datos);
        return ResponseEntity.ok(nuevaNota);
    }

    @GetMapping(path = {"","/"})
    @Operation(summary = "Listar notas de venta")
    public ResponseEntity<Page<DatosListarNota>> listaNotas(@PageableDefault(size = 9, sort = "fecha")Pageable paginacion){
        var page = notaVentaService.listarNotas(paginacion);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Buscar nota de venta por ID")
    public ResponseEntity<DatosDetalleNota> buscarNotaId(@PathVariable String id){
        var nota = notaVentaService.buscarNotaId(id);
        return ResponseEntity.ok().body(nota);
    }

    @PatchMapping(path = "/{id}")
    @Operation(summary = "Actualizar nota de venta")
    public ResponseEntity<DatosDetalleNota> actualizarNota(@PathVariable String id, @RequestBody @Valid DatosActualizarNota datos){
        var notaActualizada = notaVentaService.actualizarNota(id, datos);
        return ResponseEntity.ok().body(notaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar nota de venta")
    public ResponseEntity<Void> eliminarNota(@PathVariable String id){
        notaVentaService.eliminarNota(id);
        return ResponseEntity.noContent().build();
    }

}
