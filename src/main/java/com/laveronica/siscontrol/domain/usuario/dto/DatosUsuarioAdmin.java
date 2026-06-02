package com.laveronica.siscontrol.domain.usuario.dto;

import com.laveronica.siscontrol.enums.Role;

public record DatosUsuarioAdmin(
        String id,
        String username,
        String role,
        Boolean activo
) {
}
