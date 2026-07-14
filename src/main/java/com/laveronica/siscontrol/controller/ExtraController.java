package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.extra.dto.DatosListarExtra;
import com.laveronica.siscontrol.domain.extra.dto.DatosRegistroExtra;
import com.laveronica.siscontrol.services.ExtraService;
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
@RequestMapping("/extras")
@RequiredArgsConstructor
@Tag(name = "Extras")
@SecurityRequirement(name = "bearerAuth")
public class ExtraController {

    private final ExtraService extraService;

    @PostMapping
    @Operation(summary = "Crear extra")
    public ResponseEntity<DatosListarExtra> crear(@Valid @RequestBody DatosRegistroExtra datos,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(extraService.crearExtra(datos, userDetails.getUsername()));
    }

    @GetMapping("/orden/{ordenCompraId}")
    @Operation(summary = "Listar extras por orden de compra")
    public ResponseEntity<List<DatosListarExtra>> listarPorOrden(@PathVariable String ordenCompraId) {
        return ResponseEntity.ok(extraService.listarPorOrden(ordenCompraId));
    }

    @PostMapping("/{id}/firmar")
    @Operation(summary = "Firmar extra")
    public ResponseEntity<DatosListarExtra> firmar(@PathVariable String id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(extraService.firmarExtra(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar extra (solo si no está firmado)")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        extraService.eliminarExtra(id);
        return ResponseEntity.noContent().build();
    }
}
