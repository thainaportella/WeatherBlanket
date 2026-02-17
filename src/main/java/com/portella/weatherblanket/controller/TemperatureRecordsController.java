package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.entities.DTOs.RegistroDTO;
import com.portella.weatherblanket.repositories.TemperatureRepository;
import com.portella.weatherblanket.service.TemperatureSchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TemperatureRecordsController {
    private final TemperatureRepository temperatureRepository;
    private final TemperatureSchedulerService temperatureService;

    public TemperatureRecordsController(
            TemperatureRepository temperatureRepository,
            TemperatureSchedulerService temperatureService
    ) {
        this.temperatureRepository = temperatureRepository;
        this.temperatureService = temperatureService;
    }

    @GetMapping("/temperature-records")
    public List<RegistroDTO> listRecords(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false) Integer limit
    ) throws Exception {
        return temperatureRepository.listRecords(month, year, order, limit);
    }

    @PutMapping("/temperature-records/{date}/done")
    public ResponseEntity<Void> checkAsDone(@PathVariable LocalDate date) {
        temperatureService.checkAsDone(date);
        return ResponseEntity.noContent().build();
    }
}
