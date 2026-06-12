package com.laveronica.siscontrol.domain.usuario.dto;

import com.laveronica.siscontrol.enums.Accion;
import com.laveronica.siscontrol.enums.Modulo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "UsuarioDetalle")
public record DatosDetalleUsuario(
        String id,
        String username,
        String role,
        Boolean activo,
        String nombreCompleto,
        String correo,
        String numero,
        String cargo,
        @Schema(description = "Lista de permisos asignados")
        List<PermisoAsignado> permisos
) {
    @Schema(name = "UsuarioPermisoAsignado", description = "Permiso asignado al usuario")
    public record PermisoAsignado(
            @Schema(description = "Módulo del sistema")
            Modulo modulo,
            @Schema(description = "Acción del módulo")
            Accion accion
    ) {}
}
