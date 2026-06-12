package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.usuario.dto.*;
import com.laveronica.siscontrol.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<DatosRespuestaAuth> login(@RequestBody @Valid DatosLogin datos) {
        DatosRespuestaAuth respuesta = authService.login(datos);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario")
    public ResponseEntity<DatosRespuestaAuth> register(@RequestBody @Valid DatosRegistroUsuario datos) {
        DatosRespuestaAuth respuesta = authService.register(datos);
        return ResponseEntity.ok(respuesta);
    }
}
