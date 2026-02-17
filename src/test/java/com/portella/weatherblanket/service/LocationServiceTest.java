package com.portella.weatherblanket.service;

import com.portella.weatherblanket.exceptions.ServiceException;
import com.portella.weatherblanket.model.LocationResponse;
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
    private NominatimClientService nominatimClientService;

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

        Mockito.when(nominatimClientService.getAddress(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(jsonMock);

        LocationResponse result =
                locationService.getLocation(-22.41, -43.17);

        assertEquals("Petrópolis", result.getCity());
        assertEquals("RJ", result.getState());
        assertEquals("Brasil", result.getCountry());
    }

    @Test
    void deveLancarServiceExceptionQuandoApiFalhar() {

        Mockito.when(nominatimClientService.getAddress(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenThrow(new RuntimeException("erro externo"));

        assertThrows(
                ServiceException.class,
                () -> locationService.getLocation(-22.41, -43.17)
        );
    }
}
