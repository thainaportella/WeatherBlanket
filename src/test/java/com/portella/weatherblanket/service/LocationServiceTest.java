package com.portella.weatherblanket.service;

import com.portella.weatherblanket.entities.NominatimClient;
import com.portella.weatherblanket.exceptions.ServiceException;
import com.portella.weatherblanket.model.Localizacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private NominatimClient nominatimClient;

    @InjectMocks
    private LocationService locationService;

    @Test
    void deveBuscarLocalizacaoComSucesso() throws Exception {

        String jsonMock = """
            {
              "address": {
                "city": "Petrópolis",
                "state": "RJ",
                "country": "Brasil"
              }
            }
            """;

        Mockito.when(nominatimClient.buscarEndereco(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(jsonMock);

        Localizacao result =
                locationService.buscarLocalizacao(-22.41, -43.17);

        assertEquals("Petrópolis", result.getCidade());
        assertEquals("RJ", result.getEstado());
        assertEquals("Brasil", result.getPais());
    }

    @Test
    void deveLancarServiceExceptionQuandoApiFalhar() {

        Mockito.when(nominatimClient.buscarEndereco(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenThrow(new RuntimeException("erro externo"));

        assertThrows(
                ServiceException.class,
                () -> locationService.buscarLocalizacao(-22.41, -43.17)
        );
    }
}
