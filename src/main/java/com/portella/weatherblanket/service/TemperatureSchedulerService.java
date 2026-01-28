package com.portella.weatherblanket.service;

import com.portella.weatherblanket.config.LocationConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
@Service
public class TemperatureSchedulerService {

    private static final ZoneId ZONA_BRASIL = ZoneId.of("America/Sao_Paulo");
    private static final int REGISTERING_HOUR = 19;

    private final WeatherClient weatherClient;
    private final TemperatureRepository repository;

    private LocalDate ultimaDataRegistrada;

    public TemperatureSchedulerService(
            WeatherClient weatherClient,
            TemperatureRepository repository
    ) {
        this.weatherClient = weatherClient;
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        this.ultimaDataRegistrada = repository.buscarUltimaData();
    }

    @Scheduled(fixedRate = 60_000)
    public void registrarTemperatura() {

        LocalDateTime agora = LocalDateTime.now(ZONA_BRASIL);
        LocalDate hoje = agora.toLocalDate();

        if (ultimaDataRegistrada != null && ultimaDataRegistrada.equals(hoje)) {
            return;
        }

        if (agora.getHour() < REGISTERING_HOUR) {
            return;
        }

        double temperatura =
                weatherClient.buscarTemperatura(
                        LocationConfig.getLatitude(),
                        LocationConfig.getLongitude()
                );

        if (temperatura == Double.MIN_VALUE) return;

        repository.salvar(temperatura);
        ultimaDataRegistrada = hoje;
    }
}
