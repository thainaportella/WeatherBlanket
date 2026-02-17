package com.portella.weatherblanket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;
    private final String ADMIN_USER = "admin";
    private final String ADMIN_PASS = "senha_segura";

    @BeforeEach
    void setup() {
        authService = new AuthService();

        ReflectionTestUtils.setField(authService, "adminLogin", ADMIN_USER);
        ReflectionTestUtils.setField(authService, "adminPassword", ADMIN_PASS);
    }

    @Test
    @DisplayName("Deve retornar UserDetails quando o username for igual ao admin configurado")
    void deveCarregarUsuarioQuandoUsernameValido() {

        UserDetails userDetails = authService.loadUserByUsername(ADMIN_USER);

        assertNotNull(userDetails);
        assertEquals(ADMIN_USER, userDetails.getUsername());
        assertEquals(ADMIN_PASS, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o username for diferente do configurado")
    void deveLancarExceptionQuandoUsernameInvalido() {
        String usuarioErrado = "outro_usuario";

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername(usuarioErrado);
        });
    }

    @Test
    @DisplayName("Deve garantir que o erro contenha a mensagem correta")
    void deveVerificarMensagemDeErro() {
        String usuarioErrado = "inexistente";

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername(usuarioErrado);
        });

        assertTrue(exception.getMessage().contains("User not found: " + usuarioErrado));
    }
}