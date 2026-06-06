package com.laveronica.siscontrol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laveronica.siscontrol.domain.usuario.dto.DatosDetalleUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosPermisoUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRegistroUsuario;
import com.laveronica.siscontrol.domain.usuario.dto.DatosRespuestaAuth;
import com.laveronica.siscontrol.domain.usuario.dto.DatosUsuarioAdmin;
import com.laveronica.siscontrol.enums.Role;
import com.laveronica.siscontrol.infra.security.JwtUtil;
import com.laveronica.siscontrol.services.UsuarioAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UsuarioAdminController.class)
class UsuarioAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioAdminService usuarioAdminService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void listar_DeberiaRetornar200() throws Exception {
        var usuario = new DatosUsuarioAdmin("1", "admin", "ADMIN", true);

        given(usuarioAdminService.listarUsuarios()).willReturn(List.of(usuario));

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("admin"));
    }

    @Test
    @WithMockUser
    void buscarPorId_DeberiaRetornar200() throws Exception {
        var usuario = new DatosDetalleUsuario("1", "admin", "ADMIN", true, List.of());

        given(usuarioAdminService.buscarUsuario("1")).willReturn(usuario);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("admin"));
    }

    @Test
    @WithMockUser
    void crear_DeberiaRetornar200() throws Exception {
        var request = new DatosRegistroUsuario("nuevousuario", "password", "USER");
        var response = new DatosRespuestaAuth(null, "nuevousuario", Role.USER, "Bearer");

        given(usuarioAdminService.crearUsuario(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("nuevousuario"));
    }

    @Test
    @WithMockUser
    void asignarPermisos_DeberiaRetornar204() throws Exception {
        var permiso = new DatosPermisoUsuario.DatosPermiso("PRODUCTOS", "LECTURA");
        var request = new DatosPermisoUsuario("1", List.of(permiso));

        doNothing().when(usuarioAdminService).asignarPermisos(anyString(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/1/permisos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    void toggleUsuario_DeberiaRetornar204() throws Exception {
        doNothing().when(usuarioAdminService).toggleUsuario("1");

        mockMvc.perform(MockMvcRequestBuilders.patch("/usuarios/1/toggle")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
