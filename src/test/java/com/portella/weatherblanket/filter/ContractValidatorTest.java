package com.portella.weatherblanket.filter;

import com.portella.weatherblanket.exceptions.ContractViolationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(
        strictness = org.mockito.quality.Strictness.LENIENT
)
class ContractValidatorTest {


    @Mock
    private HttpServletRequest request;

    @Mock
    private EndpointContract contract;

    @Test
    void devePassarQuandoContratoForValido() {

        when(request.getMethod()).thenReturn("GET");
        when(request.getParameterMap()).thenReturn(Map.of());

        when(contract.getMethod()).thenReturn("GET");
        when(contract.getQueryParams()).thenReturn(Set.of());
        when(contract.isBodyRequired()).thenReturn(false);

        assertDoesNotThrow(() ->
                ContractValidator.validate(request, contract)
        );
    }

    @Test
    void deveLancarErroQuandoMetodoForInvalido() {

        when(request.getMethod()).thenReturn("POST");
        when(contract.getMethod()).thenReturn("GET");

        ContractViolationException ex = assertThrows(
                ContractViolationException.class,
                () -> ContractValidator.validate(request, contract)
        );

        assertEquals(405, ex.getStatus());
        assertEquals("METHOD_NOT_ALLOWED", ex.getError());
    }

    @Test
    void deveLancarErroQuandoParametroNaoExisteNoContrato() {

        when(request.getMethod()).thenReturn("GET");
        when(contract.getMethod()).thenReturn("GET");

        when(request.getParameterMap())
                .thenReturn(Map.of("invalidParam", new String[]{"1"}));

        when(contract.getQueryParams()).thenReturn(Set.of("limit"));
        when(contract.isBodyRequired()).thenReturn(false);

        ContractViolationException ex = assertThrows(
                ContractViolationException.class,
                () -> ContractValidator.validate(request, contract)
        );

        assertEquals(400, ex.getStatus());
        assertEquals("INVALID_QUERY_PARAM", ex.getError());
        assertTrue(ex.getMessage().contains("invalidParam"));
    }

    @Test
    void deveLancarErroQuandoBodyForObrigatorioENaoVier() {

        when(request.getMethod()).thenReturn("PUT");
        when(request.getContentLength()).thenReturn(0);

        when(contract.getMethod()).thenReturn("PUT");
        when(contract.getQueryParams()).thenReturn(Set.of());
        when(contract.isBodyRequired()).thenReturn(true);

        ContractViolationException ex = assertThrows(
                ContractViolationException.class,
                () -> ContractValidator.validate(request, contract)
        );

        assertEquals(400, ex.getStatus());
        assertEquals("BODY_REQUIRED", ex.getError());
    }
}
