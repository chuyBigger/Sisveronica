package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.reporte.dto.DatosReporteProduccionCarne;
import com.laveronica.siscontrol.services.ReporteProduccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes")
@SecurityRequirement(name = "bearerAuth")
public class ReporteProduccionController {

    private final ReporteProduccionService reporteService;

    @GetMapping("/produccion-carne")
    @Operation(summary = "Reporte semanal de producción de carnes")
    public ResponseEntity<DatosReporteProduccionCarne> reporteSemanal(
            @RequestParam("semana") LocalDate fechaInicioSemana) {
        return ResponseEntity.ok(reporteService.generarReporte(fechaInicioSemana));
    }
}
