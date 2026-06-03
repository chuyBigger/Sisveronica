package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/enums")
@Tag(name = "Enums")
@SecurityRequirement(name = "bearerAuth")
public class EnumsController {

    @GetMapping("/partidas")
    @Operation(summary = "Listar partidas")
    public ResponseEntity<List<String>> listarPartidas() {
        return ResponseEntity.ok(Arrays.stream(Partida.values()).map(Enum::name).toList());
    }

    @GetMapping("/unidades-medida")
    @Operation(summary = "Listar unidades de medida")
    public ResponseEntity<List<String>> listarUnidadesMedida() {
        return ResponseEntity.ok(Arrays.stream(UnidadMedida.values()).map(Enum::name).toList());
    }
}
