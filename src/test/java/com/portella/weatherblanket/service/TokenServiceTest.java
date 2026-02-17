package com.portella.weatherblanket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    private final String SECRET_KEY = "minha-chave-secreta-muito-segura-e-longa";

    @BeforeEach
    void setup() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", SECRET_KEY);
    }

    @Test
    @DisplayName("Deve gerar um token válido para um login específico")
    void deveGerarToken() {
        String login = "usuario@teste.com";

        String token = tokenService.generateToken(login);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar o subject (login) ao validar um token legítimo")
    void deveValidarTokenCorretamente() {
        String login = "admin";
        String token = tokenService.generateToken(login);

        String subject = tokenService.validateToken(token);

        assertEquals(login, subject);
    }

    @Test
    @DisplayName("Deve retornar string vazia quando o token for inválido ou estiver malformado")
    void deveRetornarVazioParaTokenInvalido() {
        String tokenInvalido = "token.totalmente.errado";

        String subject = tokenService.validateToken(tokenInvalido);

        assertEquals("", subject);
    }

    @Test
    @DisplayName("Deve retornar vazio se o token for assinado com uma chave diferente")
    void deveRejeitarTokenComAssinaturaDiferente() {
        String token = tokenService.generateToken("admin");

        ReflectionTestUtils.setField(tokenService, "secret", "outra-chave-qualquer-muito-diferente");

        String subject = tokenService.validateToken(token);

        assertEquals("", subject);
    }
}