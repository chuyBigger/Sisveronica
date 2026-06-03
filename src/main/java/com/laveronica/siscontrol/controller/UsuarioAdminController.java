package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.usuario.dto.*;
import com.laveronica.siscontrol.services.UsuarioAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios (Admin)")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioAdminController {

    private final UsuarioAdminService usuarioAdminService;

    @GetMapping
    @Operation(summary = "Listar usuarios")
    public ResponseEntity<List<DatosUsuarioAdmin>> listar() {
        return ResponseEntity.ok(usuarioAdminService.listarUsuarios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    public ResponseEntity<DatosDetalleUsuario> buscar(@PathVariable String id) {
        return ResponseEntity.ok(usuarioAdminService.buscarUsuario(id));
    }

    @PostMapping
    @Operation(summary = "Crear usuario")
    public ResponseEntity<DatosRespuestaAuth> crear(@RequestBody @Valid DatosRegistroUsuario datos) {
        DatosRespuestaAuth respuesta = usuarioAdminService.crearUsuario(datos);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}/permisos")
    @Operation(summary = "Asignar permisos a usuario")
    public ResponseEntity<Void> asignarPermisos(@PathVariable String id, @RequestBody DatosPermisoUsuario datos) {
        usuarioAdminService.asignarPermisos(id, datos.permisos());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Activar o desactivar usuario")
    public ResponseEntity<Void> toggleUsuario(@PathVariable String id) {
        usuarioAdminService.toggleUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
