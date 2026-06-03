package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosListarCancelacion;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosRegistroCancelacion;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.services.NotaCancelacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cancelaciones")
@Tag(name = "Cancelaciones")
@SecurityRequirement(name = "bearerAuth")
public class NotaCancelacionController {

    @Autowired
    private NotaCancelacionService cancelacionService;

    @PostMapping
    @Operation(summary = "Crear cancelación")
    public ResponseEntity<DatosListarCancelacion> crear(@Valid @RequestBody DatosRegistroCancelacion datos) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var cancelacion = cancelacionService.crearCancelacion(datos, username);
        return ResponseEntity.ok(cancelacion);
    }

    @GetMapping("/orden/{ordenCompraId}")
    @Operation(summary = "Listar cancelaciones por orden")
    public ResponseEntity<List<DatosListarCancelacion>> listarPorOrden(@PathVariable String ordenCompraId) {
        var list = cancelacionService.listarPorOrden(ordenCompraId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/validar")
    @Operation(summary = "Validar cancelación")
    public ResponseEntity<DatosListarCancelacion> validar(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var cancelacion = cancelacionService.validarCancelacion(id, username);
        return ResponseEntity.ok(cancelacion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cancelación")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        cancelacionService.eliminarCancelacion(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reconstruir/{ordenCompraId}")
    @Operation(summary = "Reconstruir notas canceladas")
    public ResponseEntity<List<DatosDetalleNota>> reconstruir(@PathVariable String ordenCompraId) {
        var notas = cancelacionService.reconstruirNotas(ordenCompraId);
        return ResponseEntity.ok(notas);
    }
}
