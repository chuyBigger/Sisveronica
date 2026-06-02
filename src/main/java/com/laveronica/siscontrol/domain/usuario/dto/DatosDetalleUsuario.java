package com.laveronica.siscontrol.domain.usuario.dto;

import com.laveronica.siscontrol.enums.Accion;
import com.laveronica.siscontrol.enums.Modulo;

import java.util.List;

public record DatosDetalleUsuario(
        String id,
        String username,
        String role,
        Boolean activo,
        List<PermisoAsignado> permisos
) {
    public record PermisoAsignado(
            Modulo modulo,
            Accion accion
    ) {}
}
