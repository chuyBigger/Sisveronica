package com.laveronica.siscontrol.domain.usuario.dto;

import com.laveronica.siscontrol.enums.Role;

public record DatosRespuestaAuth(
        String token,
        String username,
        Role role,
        String tipo
) {
}
