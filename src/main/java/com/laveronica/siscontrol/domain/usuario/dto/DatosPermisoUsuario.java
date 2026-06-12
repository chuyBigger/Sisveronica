package com.laveronica.siscontrol.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Schema(name = "UsuarioPermiso")
public record DatosPermisoUsuario(
        @NotBlank String usuarioId,
        @Schema(description = "Lista de permisos asignados")
        List<DatosPermiso> permisos
) {
    @Schema(name = "UsuarioPermisoItem", description = "Permiso del usuario")
    public record DatosPermiso(
            @Schema(description = "Módulo del sistema")
            String modulo,
            @Schema(description = "Acción del módulo")
            String accion
    ) {}
}
