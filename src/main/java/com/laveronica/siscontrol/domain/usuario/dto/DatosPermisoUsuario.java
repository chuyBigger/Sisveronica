package com.laveronica.siscontrol.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record DatosPermisoUsuario(
        @NotBlank String usuarioId,
        List<DatosPermiso> permisos
) {
    public record DatosPermiso(
            String modulo,
            String accion
    ) {}
}
