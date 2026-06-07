package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laveronica.siscontrol.domain.usuario.dto.DatosLogin;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRegistroUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRespuestaAuth;
import com.laveronica.siscontrol.enums.Role;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void login_DeberiaRetornar200() throws Exception {
        var request = new DatosLogin("usuario", "password");
        var response = new DatosRespuestaAuth("token-jwt", "usuario", Role.USER, "Bearer");

        given(authService.login(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token-jwt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("usuario"));
    }

    @Test
    @WithMockUser
    void register_DeberiaRetornar200() throws Exception {
        var request = new DatosRegistroUsuario("nuevousuario", "password", "USER", null, null, null, null);
        var response = new DatosRespuestaAuth("token-jwt", "nuevousuario", Role.USER, "Bearer");

        given(authService.register(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("nuevousuario"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("USER"));
    }
}
