package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.usuario.Usuario;
import com.laveronica.siscontrol.domain.usuario.UsuarioPermiso;
import com.laveronica.siscontrol.domain.usuario.dto.*;
import com.laveronica.siscontrol.enums.Accion;
import com.laveronica.siscontrol.enums.Modulo;
import com.laveronica.siscontrol.enums.Role;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.UsuarioPermisoRepository;
import com.laveronica.siscontrol.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioAdminService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioPermisoRepository permisoRepository;
    private final PasswordEncoder passwordEncoder;

    public List<DatosUsuarioAdmin> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new DatosUsuarioAdmin(
                        u.getId(), u.getUsername(), u.getRole().name(), u.getActivo(),
                        u.getNombreCompleto(), u.getCorreo(), u.getNumero(), u.getCargo()))
                .collect(Collectors.toList());
    }

    public DatosDetalleUsuario buscarUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<UsuarioPermiso> permisos = permisoRepository.findByUsuarioId(id);

        List<DatosDetalleUsuario.PermisoAsignado> permisosDto = permisos.stream()
                .map(p -> new DatosDetalleUsuario.PermisoAsignado(p.getModulo(), p.getAccion()))
                .collect(Collectors.toList());

        return new DatosDetalleUsuario(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole().name(),
                usuario.getActivo(),
                usuario.getNombreCompleto(),
                usuario.getCorreo(),
                usuario.getNumero(),
                usuario.getCargo(),
                permisosDto
        );
    }

    public DatosRespuestaAuth crearUsuario(DatosRegistroUsuario datos) {
        if (usuarioRepository.existsByUsername(datos.username())) {
            throw new RecursoExistenteException("El usuario ya existe: " + datos.username());
        }

        Role role = datos.role() != null ? Role.valueOf(datos.role()) : Role.USER;

        Usuario usuario = new Usuario(
                datos.username(),
                passwordEncoder.encode(datos.password()),
                role
        );
        usuario.setNombreCompleto(datos.nombreCompleto());
        usuario.setCorreo(datos.correo());
        usuario.setNumero(datos.numero());
        usuario.setCargo(datos.cargo());

        usuarioRepository.save(usuario);

        return new DatosRespuestaAuth(null, usuario.getUsername(), usuario.getRole(), "Bearer");
    }

    @Transactional
    public void asignarPermisos(String usuarioId, List<DatosPermisoUsuario.DatosPermiso> permisos) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        permisoRepository.deleteByUsuarioId(usuarioId);

        for (DatosPermisoUsuario.DatosPermiso p : permisos) {
            UsuarioPermiso permiso = UsuarioPermiso.builder()
                    .usuario(usuario)
                    .modulo(Modulo.valueOf(p.modulo()))
                    .accion(Accion.valueOf(p.accion()))
                    .build();
            permisoRepository.save(permiso);
        }
    }

    @Transactional
    public void toggleUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public DatosUsuarioAdmin actualizarUsuario(String id, DatosActualizarUsuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (datos.nombreCompleto() != null) usuario.setNombreCompleto(datos.nombreCompleto());
        if (datos.correo() != null) usuario.setCorreo(datos.correo());
        if (datos.numero() != null) usuario.setNumero(datos.numero());
        if (datos.cargo() != null) usuario.setCargo(datos.cargo());
        if (datos.password() != null && !datos.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(datos.password()));
        }
        if (datos.role() != null) {
            usuario.setRole(Role.valueOf(datos.role()));
        }
        usuarioRepository.save(usuario);
        return new DatosUsuarioAdmin(
                usuario.getId(), usuario.getUsername(), usuario.getRole().name(), usuario.getActivo(),
                usuario.getNombreCompleto(), usuario.getCorreo(), usuario.getNumero(), usuario.getCargo());
    }
}
