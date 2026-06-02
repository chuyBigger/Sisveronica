package com.laveronica.siscontrol.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatosRegistroUsuario(
        @NotBlank String username,
        @NotBlank @Size(min = 4) String password,
        String role
) {
}
