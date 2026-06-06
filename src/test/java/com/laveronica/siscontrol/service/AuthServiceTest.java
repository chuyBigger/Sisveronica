package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.usuario.Usuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosLogin;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRegistroUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRespuestaAuth;
import com.laveronica.siscontrol.enums.Role;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.RuleValidationException;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.repositories.UsuarioRepository;
import com.laveronica.siscontrol.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginSuccess() {
        var datos = new DatosLogin("admin", "password123");
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setRole(Role.ADMIN);

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        given(usuarioRepository.findByUsernameAndActivoTrue("admin")).willReturn(Optional.of(usuario));
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(userDetails);
        given(jwtUtil.generateToken(userDetails)).willReturn("jwt-token-123");

        DatosRespuestaAuth result = authService.login(datos);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("jwt-token-123");
        assertThat(result.username()).isEqualTo("admin");
        assertThat(result.role()).isEqualTo(Role.ADMIN);
        assertThat(result.tipo()).isEqualTo("Bearer");
    }

    @Test
    void loginUserNotFoundThrowsUsernameNotFoundException() {
        var datos = new DatosLogin("unknown", "password123");

        given(usuarioRepository.findByUsernameAndActivoTrue("unknown")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(datos))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("El usuario no existe");
    }

    @Test
    void loginBadCredentialsThrowsRuleValidationException() {
        var datos = new DatosLogin("admin", "wrong-password");
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setRole(Role.ADMIN);

        given(usuarioRepository.findByUsernameAndActivoTrue("admin")).willReturn(Optional.of(usuario));
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(datos))
                .isInstanceOf(RuleValidationException.class)
                .hasMessageContaining("La contraseña es incorrecta");
    }

    @Test
    void registerSuccess() {
        var datos = new DatosRegistroUsuario("nuevo-user", "password123", "USER");

        given(usuarioRepository.existsByUsername("nuevo-user")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("encoded-password");
        given(usuarioRepository.save(any())).willAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId("uuid-1");
            return u;
        });
        given(jwtUtil.generateToken(any(UserDetails.class))).willReturn("jwt-token-456");

        DatosRespuestaAuth result = authService.register(datos);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("jwt-token-456");
        assertThat(result.username()).isEqualTo("nuevo-user");
        assertThat(result.role()).isEqualTo(Role.USER);
        assertThat(result.tipo()).isEqualTo("Bearer");
        verify(usuarioRepository).save(any());
    }

    @Test
    void registerUserAlreadyExistsThrowsRecursoExistenteException() {
        var datos = new DatosRegistroUsuario("existing-user", "password123", "USER");

        given(usuarioRepository.existsByUsername("existing-user")).willReturn(true);

        assertThatThrownBy(() -> authService.register(datos))
                .isInstanceOf(RecursoExistenteException.class)
                .hasMessageContaining("El usuario ya existe");
    }

    @Test
    void registerWithDefaultRoleWhenRoleIsNull() {
        var datos = new DatosRegistroUsuario("new-user", "password123", null);

        given(usuarioRepository.existsByUsername("new-user")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("encoded");
        given(usuarioRepository.save(any())).willAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId("uuid-1");
            return u;
        });
        given(jwtUtil.generateToken(any(UserDetails.class))).willReturn("jwt-token");

        DatosRespuestaAuth result = authService.register(datos);

        assertThat(result).isNotNull();
        assertThat(result.role()).isEqualTo(Role.USER);
    }
}
