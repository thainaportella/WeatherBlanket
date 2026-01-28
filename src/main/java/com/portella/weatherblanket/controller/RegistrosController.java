package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.entities.RegistroDTO;
import com.portella.weatherblanket.repositories.TemperatureRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegistrosController {
    private final TemperatureRepository temperatureRepository;

    public RegistrosController(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @GetMapping("/registros")
    public List<RegistroDTO> listarRegistros(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false) Integer limit
    ) throws Exception {

        return temperatureRepository.listarRegistros(mes, ano, order, limit);
    }




}
