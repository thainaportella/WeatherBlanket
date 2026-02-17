package com.portella.weatherblanket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portella.weatherblanket.entities.DTOs.AuthDataDTO;
import com.portella.weatherblanket.exceptions.ServiceException;
import com.portella.weatherblanket.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("Deve retornar token JWT quando credenciais forem válidas")
    void deveRetornarTokenAoLogar() throws Exception {

        AuthDataDTO authDataDTO = new AuthDataDTO("usuario_teste", "senha123");
        String tokenGerado = "token-fake-jwt";

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        Mockito.when(tokenService.generateToken("usuario_teste"))
                .thenReturn(tokenGerado);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDataDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(tokenGerado)); // Ajuste "$.token" se o campo no TokenData tiver outro nome
    }

    @Test
    @DisplayName("Deve retornar 403 quando as credenciais estiverem incorretas")
    void deveRetornarErroAoLogarComDadosInvalidos() throws Exception {
        AuthDataDTO authDataDTO = new AuthDataDTO("usuario_invalido", "senha_errada");

        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new ServiceException(403, "FORBIDDEN", "Credenciais inválidas"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDataDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }
}