package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.repositories.NotaCancelacionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/super")
@RequiredArgsConstructor
@Tag(name = "Super Admin")
@SecurityRequirement(name = "bearerAuth")
public class SuperAdminController {

    private final NotaVentaRepository notaVentaRepository;
    private final OrdenCompraRespository ordenCompraRepository;
    private final NotaCancelacionRepository cancelacionRepository;

    @DeleteMapping("/nota/{id}")
    @Operation(summary = "Eliminar nota de venta por ID (super admin)")
    public ResponseEntity<Map<String, String>> eliminarNotaPorId(@PathVariable String id) {
        NotaVenta nota = notaVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota de venta no encontrada"));
        nota.setActivo(false);
        notaVentaRepository.save(nota);
        return ResponseEntity.ok(Map.of("mensaje", "Nota eliminada (desactivada) correctamente"));
    }

    @DeleteMapping("/nota/folio/{folio}")
    @Operation(summary = "Eliminar nota de venta por folio (super admin)")
    public ResponseEntity<Map<String, String>> eliminarNotaPorFolio(@PathVariable Integer folio) {
        NotaVenta nota = notaVentaRepository.findByFolio(folio)
                .orElseThrow(() -> new RuntimeException("Nota de venta no encontrada con folio " + folio));
        nota.setActivo(false);
        notaVentaRepository.save(nota);
        return ResponseEntity.ok(Map.of("mensaje", "Nota eliminada (desactivada) correctamente"));
    }

    @DeleteMapping("/orden/{id}")
    @Operation(summary = "Eliminar orden de compra por ID (super admin)")
    public ResponseEntity<Map<String, String>> eliminarOrdenPorId(@PathVariable String id) {
        OrdenCompra oc = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));
        oc.setActivo(false);
        ordenCompraRepository.save(oc);
        return ResponseEntity.ok(Map.of("mensaje", "Orden de compra eliminada (desactivada) correctamente"));
    }

    @DeleteMapping("/cancelacion/{id}")
    @Operation(summary = "Eliminar cancelación por ID (super admin)")
    public ResponseEntity<Map<String, String>> eliminarCancelacionPorId(@PathVariable String id) {
        NotaCancelacion nc = cancelacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada"));
        nc.setActivo(false);
        cancelacionRepository.save(nc);
        return ResponseEntity.ok(Map.of("mensaje", "Cancelación eliminada (desactivada) correctamente"));
    }
}
