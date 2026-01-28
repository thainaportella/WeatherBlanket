package com.portella.weatherblanket.service;

import com.portella.weatherblanket.entities.WeatherClient;
import com.portella.weatherblanket.repositories.TemperatureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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


        Mockito.when(weatherClient.buscarTemperatura(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(25.0);

        service.registrarTemperatura();

        Mockito.verify(repository).salvar(25.0);
    }

    @Test
    void naoDeveSalvarQuandoTemperaturaInvalida() {

        Mockito.when(weatherClient.buscarTemperatura(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(Double.MIN_VALUE);

        service.registrarTemperatura();

        Mockito.verify(repository, Mockito.never()).salvar(Mockito.anyDouble());
    }
}

