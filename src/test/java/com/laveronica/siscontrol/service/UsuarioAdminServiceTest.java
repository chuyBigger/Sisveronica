package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.usuario.Usuario;
import com.laveronica.siscontrol.domain.usuario.UsuarioPermiso;
import com.laveronica.siscontrol.domain.usuario.dto.DatosDetalleUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosPermisoUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRegistroUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRespuestaAuth;
import com.laveronica.siscontrol.domain.usuario.dto.DatosUsuarioAdmin;
import com.laveronica.siscontrol.enums.Accion;
import com.laveronica.siscontrol.enums.Modulo;
import com.laveronica.siscontrol.enums.Role;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.UsuarioPermisoRepository;
import com.laveronica.siscontrol.repositories.UsuarioRepository;
import com.laveronica.siscontrol.services.UsuarioAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UsuarioAdminServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioPermisoRepository permisoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioAdminService usuarioAdminService;

    @Test
    void listarUsuariosReturnsAllUsers() {
        Usuario u1 = new Usuario();
        u1.setId("1");
        u1.setUsername("admin");
        u1.setRole(Role.ADMIN);
        u1.setActivo(true);

        Usuario u2 = new Usuario();
        u2.setId("2");
        u2.setUsername("user1");
        u2.setRole(Role.USER);
        u2.setActivo(false);

        given(usuarioRepository.findAll()).willReturn(List.of(u1, u2));

        List<DatosUsuarioAdmin> result = usuarioAdminService.listarUsuarios();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).username()).isEqualTo("admin");
        assertThat(result.get(0).role()).isEqualTo("ADMIN");
        assertThat(result.get(1).username()).isEqualTo("user1");
    }

    @Test
    void buscarUsuarioFound() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsername("admin");
        usuario.setRole(Role.ADMIN);
        usuario.setActivo(true);

        UsuarioPermiso permiso = UsuarioPermiso.builder()
                .modulo(Modulo.PRODUCTOS)
                .accion(Accion.CREAR)
                .build();

        given(usuarioRepository.findById("1")).willReturn(Optional.of(usuario));
        given(permisoRepository.findByUsuarioId("1")).willReturn(List.of(permiso));

        DatosDetalleUsuario result = usuarioAdminService.buscarUsuario("1");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
        assertThat(result.username()).isEqualTo("admin");
        assertThat(result.role()).isEqualTo("ADMIN");
        assertThat(result.activo()).isTrue();
        assertThat(result.permisos()).hasSize(1);
        assertThat(result.permisos().get(0).modulo()).isEqualTo(Modulo.PRODUCTOS);
        assertThat(result.permisos().get(0).accion()).isEqualTo(Accion.CREAR);
    }

    @Test
    void buscarUsuarioNotFoundThrowsResourceNotFoundException() {
        given(usuarioRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioAdminService.buscarUsuario("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void crearUsuarioSuccess() {
        var datos = new DatosRegistroUsuario("newuser", "password123", "USER");

        given(usuarioRepository.existsByUsername("newuser")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("encoded-password");
        given(usuarioRepository.save(any())).willAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId("uuid-1");
            return u;
        });

        DatosRespuestaAuth result = usuarioAdminService.crearUsuario(datos);

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("newuser");
        assertThat(result.role()).isEqualTo(Role.USER);
        assertThat(result.tipo()).isEqualTo("Bearer");
        assertThat(result.token()).isNull();
        verify(usuarioRepository).save(any());
    }

    @Test
    void crearUsuarioAlreadyExistsThrowsRecursoExistenteException() {
        var datos = new DatosRegistroUsuario("existing", "password123", "USER");

        given(usuarioRepository.existsByUsername("existing")).willReturn(true);

        assertThatThrownBy(() -> usuarioAdminService.crearUsuario(datos))
                .isInstanceOf(RecursoExistenteException.class)
                .hasMessageContaining("El usuario ya existe");
    }

    @Test
    void crearUsuarioWithDefaultRoleWhenRoleIsNull() {
        var datos = new DatosRegistroUsuario("newuser", "password123", null);

        given(usuarioRepository.existsByUsername("newuser")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("encoded");
        given(usuarioRepository.save(any())).willAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId("uuid-1");
            return u;
        });

        DatosRespuestaAuth result = usuarioAdminService.crearUsuario(datos);

        assertThat(result.role()).isEqualTo(Role.USER);
    }

    @Test
    void asignarPermisosSuccess() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsername("target-user");

        var permiso = new DatosPermisoUsuario.DatosPermiso("PRODUCTOS", "CREAR");
        var datos = new DatosPermisoUsuario("1", List.of(permiso));

        given(usuarioRepository.findById("1")).willReturn(Optional.of(usuario));

        usuarioAdminService.asignarPermisos("1", datos.permisos());

        verify(permisoRepository).deleteByUsuarioId("1");
        verify(permisoRepository).save(any(UsuarioPermiso.class));
    }

    @Test
    void asignarPermisosUsuarioNotFoundThrowsResourceNotFoundException() {
        var permiso = new DatosPermisoUsuario.DatosPermiso("PRODUCTOS", "CREAR");

        given(usuarioRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioAdminService.asignarPermisos("bad-id", List.of(permiso)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void toggleUsuarioSuccess() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsername("user1");
        usuario.setActivo(true);

        given(usuarioRepository.findById("1")).willReturn(Optional.of(usuario));
        given(usuarioRepository.save(usuario)).willReturn(usuario);

        usuarioAdminService.toggleUsuario("1");

        assertThat(usuario.getActivo()).isFalse();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void toggleUsuarioReactivatesInactiveUser() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsername("user1");
        usuario.setActivo(false);

        given(usuarioRepository.findById("1")).willReturn(Optional.of(usuario));
        given(usuarioRepository.save(usuario)).willReturn(usuario);

        usuarioAdminService.toggleUsuario("1");

        assertThat(usuario.getActivo()).isTrue();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void toggleUsuarioNotFoundThrowsResourceNotFoundException() {
        given(usuarioRepository.findById("bad-id")).willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioAdminService.toggleUsuario("bad-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}
