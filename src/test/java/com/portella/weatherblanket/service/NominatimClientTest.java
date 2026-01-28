package com.portella.weatherblanket.service;

import com.portella.weatherblanket.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NominatimClientTest {

    @Mock
    private HttpConnectionFactory factory;

    @Mock
    private HttpURLConnection connection;

    @InjectMocks
    private NominatimClient client;

    @Test
    void deveBuscarEnderecoComSucesso() throws Exception {

        String json = "{ \"address\": { \"city\": \"Petrópolis\" } }";

        when(factory.create(any(URL.class))).thenReturn(connection);
        when(connection.getInputStream())
                .thenReturn(new ByteArrayInputStream(json.getBytes()));

        String response = client.buscarEndereco(-22.4, -43.1);

        assertNotNull(response);
        assertTrue(response.contains("Petrópolis"));
    }

    @Test
    void deveLancarServiceExceptionQuandoFalhar() throws Exception {

        when(factory.create(any(URL.class)))
                .thenThrow(new RuntimeException("erro"));

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> client.buscarEndereco(-22.4, -43.1)
        );

        assertEquals(502, ex.getStatus());
        assertEquals("SEARCH_LOCATION_ERROR", ex.getError());
    }
}
