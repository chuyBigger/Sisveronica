package com.laveronica.siscontrol.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UsuarioLogin")
public record DatosLogin(
        @NotBlank String username,
        @NotBlank @Size(min = 4) String password
) {
}
