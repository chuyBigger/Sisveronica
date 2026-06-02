package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.usuario.dto.*;
import com.laveronica.siscontrol.services.UsuarioAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private final UsuarioAdminService usuarioAdminService;

    @GetMapping
    public ResponseEntity<List<DatosUsuarioAdmin>> listar() {
        return ResponseEntity.ok(usuarioAdminService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleUsuario> buscar(@PathVariable String id) {
        return ResponseEntity.ok(usuarioAdminService.buscarUsuario(id));
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaAuth> crear(@RequestBody @Valid DatosRegistroUsuario datos) {
        DatosRespuestaAuth respuesta = usuarioAdminService.crearUsuario(datos);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}/permisos")
    public ResponseEntity<Void> asignarPermisos(@PathVariable String id, @RequestBody DatosPermisoUsuario datos) {
        usuarioAdminService.asignarPermisos(id, datos.permisos());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleUsuario(@PathVariable String id) {
        usuarioAdminService.toggleUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
