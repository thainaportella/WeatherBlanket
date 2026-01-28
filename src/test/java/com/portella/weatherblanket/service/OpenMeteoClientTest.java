package com.portella.weatherblanket.service;

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
class OpenMeteoClientTest {

    @Mock
    private HttpConnectionFactory factory;

    @Mock
    private HttpURLConnection connection;

    @InjectMocks
    private OpenMeteoClient client;

    @Test
    void deveBuscarTemperaturaComSucesso() throws Exception {

        String json =
                "{ \"current\": { \"temperature_2m\": 26.7 } }";

        when(factory.create(any(URL.class))).thenReturn(connection);
        when(connection.getInputStream())
                .thenReturn(new ByteArrayInputStream(json.getBytes()));

        double temperatura = client.buscarTemperatura(-22.4, -43.1);

        assertEquals(26.7, temperatura);
    }

    @Test
    void deveRetornarMinValueQuandoFalhar() throws Exception {

        when(factory.create(any(URL.class)))
                .thenThrow(new RuntimeException("erro"));

        double temperatura = client.buscarTemperatura(-22.4, -43.1);

        assertEquals(Double.MIN_VALUE, temperatura);
    }
}

