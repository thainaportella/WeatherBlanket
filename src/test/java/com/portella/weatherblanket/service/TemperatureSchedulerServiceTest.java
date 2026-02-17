package com.portella.weatherblanket.service;

import com.portella.weatherblanket.entities.WeatherClient;
import com.portella.weatherblanket.repositories.TemperatureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;

import static org.mockito.ArgumentMatchers.anyDouble;

@ExtendWith(MockitoExtension.class)
class TemperatureSchedulerServiceTest {

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private TemperatureRepository repository;

    @InjectMocks
    private TemperatureSchedulerService service;

    @Test
    void deveSalvarTemperaturaQuandoHorarioValido() {
        Instant instant = LocalDateTime.of(2026, 2, 17, 15, 0).toInstant(ZoneOffset.of("-03:00"));
        Clock fixedClock = Clock.fixed(instant, ZoneId.of("America/Sao_Paulo"));

        service = new TemperatureSchedulerService(weatherClient, repository, fixedClock);

        Mockito.when(weatherClient.getTemperature(anyDouble(), anyDouble())).thenReturn(25.0);

        service.recordTemperature();

        Mockito.verify(repository).persistData(25.0);
    }

    @Test
    void naoDeveSalvarQuandoTemperaturaInvalida() {
        Instant instant = LocalDateTime.of(2026, 2, 17, 15, 0).toInstant(ZoneOffset.of("-03:00"));
        Clock fixedClock = Clock.fixed(instant, ZoneId.of("America/Sao_Paulo"));

        service = new TemperatureSchedulerService(weatherClient, repository, fixedClock);

        Mockito.when(weatherClient.getTemperature(
                anyDouble(),
                anyDouble()
        )).thenReturn(Double.MIN_VALUE);

        service.recordTemperature();

        Mockito.verify(repository, Mockito.never()).persistData(anyDouble());
    }
}

