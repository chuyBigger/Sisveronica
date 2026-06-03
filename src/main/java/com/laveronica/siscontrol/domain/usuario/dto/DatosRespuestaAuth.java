package com.laveronica.siscontrol.domain.usuario.dto;

import com.laveronica.siscontrol.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UsuarioRespuestaAuth")
public record DatosRespuestaAuth(
        String token,
        String username,
        Role role,
        String tipo
) {
}
