package com.laveronica.siscontrol.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UsuarioActualizar")
public record DatosActualizarUsuario(
        String nombreCompleto,
        String correo,
        String numero,
        String cargo,
        String password,
        String role
) {
}
