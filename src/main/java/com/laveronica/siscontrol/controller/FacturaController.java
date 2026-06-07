package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.factura.dto.DatosListarFactura;
import com.laveronica.siscontrol.domain.factura.dto.DatosRegistroFactura;
import com.laveronica.siscontrol.services.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
@Tag(name = "7. Facturas", description = "Generación y consulta de facturas/prefacturas")
@SecurityRequirement(name = "bearer-jwt")
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping
    @Operation(summary = "Generar factura para una orden de compra")
    public ResponseEntity<DatosListarFactura> generarFactura(
        @Valid @RequestBody DatosRegistroFactura dto) {
        DatosListarFactura factura = facturaService.generarFactura(dto);
        return ResponseEntity.created(URI.create("/facturas/" + factura.id()))
            .body(factura);
    }

    @GetMapping
    @Operation(summary = "Listar todas las facturas")
    public ResponseEntity<List<DatosListarFactura>> listarFacturas() {
        return ResponseEntity.ok(facturaService.listarFacturas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por ID")
    public ResponseEntity<DatosListarFactura> obtenerPorId(@PathVariable String id) {
        return facturaService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/por-orden/{ordenCompraId}")
    @Operation(summary = "Obtener factura por ID de orden de compra")
    public ResponseEntity<DatosListarFactura> obtenerPorOrdenCompraId(
        @PathVariable String ordenCompraId) {
        return facturaService.obtenerPorOrdenCompraId(ordenCompraId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
