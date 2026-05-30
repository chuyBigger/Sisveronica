package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/enums")
public class EnumsController {

    @GetMapping("/partidas")
    public ResponseEntity<List<String>> listarPartidas() {
        return ResponseEntity.ok(Arrays.stream(Partida.values()).map(Enum::name).toList());
    }

    @GetMapping("/unidades-medida")
    public ResponseEntity<List<String>> listarUnidadesMedida() {
        return ResponseEntity.ok(Arrays.stream(UnidadMedida.values()).map(Enum::name).toList());
    }
}
