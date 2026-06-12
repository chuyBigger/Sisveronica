package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.usuario.Usuario;
import com.laveronica.siscontrol.domain.usuario.dto.*;
import com.laveronica.siscontrol.enums.Role;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.RuleValidationException;
import com.laveronica.siscontrol.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public DatosRespuestaAuth login(DatosLogin datos) {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(datos.username())
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no existe: " + datos.username()));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(datos.username(), datos.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            return new DatosRespuestaAuth(token, usuario.getUsername(), usuario.getRole(), "Bearer");

        } catch (BadCredentialsException e) {
            throw new RuleValidationException("La contraseña es incorrecta");
        }
    }

    public DatosRespuestaAuth register(DatosRegistroUsuario datos) {
        if (usuarioRepository.existsByUsername(datos.username())) {
            throw new RecursoExistenteException("El usuario ya existe: " + datos.username());
        }

        Role role = datos.role() != null ? Role.valueOf(datos.role()) : Role.USER;

        Usuario usuario = new Usuario(
                datos.username(),
                passwordEncoder.encode(datos.password()),
                role
        );

        usuarioRepository.save(usuario);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                usuario.getUsername(), usuario.getPassword(),
                java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.name()))
        );

        String token = jwtUtil.generateToken(userDetails);

        return new DatosRespuestaAuth(token, usuario.getUsername(), usuario.getRole(), "Bearer");
    }
}
