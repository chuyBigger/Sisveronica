package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosListarCancelacion;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosRegistroCancelacion;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.services.NotaCancelacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cancelaciones")
@RequiredArgsConstructor
@Tag(name = "Cancelaciones")
@SecurityRequirement(name = "bearerAuth")
public class NotaCancelacionController {

    private final NotaCancelacionService cancelacionService;

    @PostMapping
    @Operation(summary = "Crear cancelación")
    public ResponseEntity<DatosListarCancelacion> crear(@Valid @RequestBody DatosRegistroCancelacion datos,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        var cancelacion = cancelacionService.crearCancelacion(datos, userDetails.getUsername());
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
    public ResponseEntity<DatosListarCancelacion> validar(@PathVariable String id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        var cancelacion = cancelacionService.validarCancelacion(id, userDetails.getUsername());
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
