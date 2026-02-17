package com.portella.weatherblanket.service;

import com.portella.weatherblanket.entities.LocationEntity;
import com.portella.weatherblanket.entities.enums.DayStatusEnum;
import com.portella.weatherblanket.entities.TemperatureEntity;
import com.portella.weatherblanket.entities.WeatherClient;
import com.portella.weatherblanket.exceptions.ServiceException;
import com.portella.weatherblanket.repositories.TemperatureRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Service
public class TemperatureSchedulerService {

    private static final int REGISTERING_HOUR = 15;
    private final Clock clock;
    private final WeatherClient weatherClient;
    private final TemperatureRepository repository;

    private LocalDate lastRecordedDate;

    public TemperatureSchedulerService(
            WeatherClient weatherClient,
            TemperatureRepository repository,
            Clock clock
    ) {
        this.weatherClient = weatherClient;
        this.repository = repository;
        this.clock = clock;
    }

    @PostConstruct
    public void init() {
        this.lastRecordedDate = repository.getLastRecordedDate();
    }

    @Scheduled(fixedRate = 60_000)
    public void recordTemperature() {
        LocalDateTime currentTime = LocalDateTime.now(clock);
        LocalDate currentDate = currentTime.toLocalDate();

        if (lastRecordedDate != null && lastRecordedDate.equals(currentDate)) {
            return;
        }

        if (currentTime.getHour() != REGISTERING_HOUR) {
            return;
        }

        LocalDate yesterday = currentDate.minusDays(1);

        repository.getRecordByDate(yesterday).ifPresent(record -> {
            if (record.getStatus() == DayStatusEnum.PENDING) {
                record.setStatus(DayStatusEnum.LATE);
                System.out.println("Record marked as LATE");
                repository.updateStatus(record);
            }
        });

        double temperature =
                weatherClient.getTemperature(
                        LocationEntity.getLatitude(),
                        LocationEntity.getLongitude()
                );

        if (temperature == Double.MIN_VALUE) return;


        repository.persistData(temperature);
        System.out.println("Temperature recorded: " + temperature);
        lastRecordedDate = currentDate;
    }

    public void checkAsDone(LocalDate date) {

        TemperatureEntity record = repository.getRecordByDate(date)
                .orElseThrow(() -> new ServiceException(
                        404,
                        "REGISTER_NOT_FOUND",
                        "Register not found for day " + date
                ));


        if (record.getStatus() != DayStatusEnum.DONE) {
            record.setStatus(DayStatusEnum.DONE);
            repository.updateStatus(record);
        }
    }
}
