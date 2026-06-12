package com.laveronica.siscontrol.domain.usuario.dto;

import com.laveronica.siscontrol.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UsuarioAdmin")
public record DatosUsuarioAdmin(
        String id,
        String username,
        String role,
        Boolean activo,
        String nombreCompleto,
        String correo,
        String numero,
        String cargo
) {
}
