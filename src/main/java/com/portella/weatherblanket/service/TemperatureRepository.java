package com.portella.weatherblanket.service;

import java.time.LocalDate;

public interface TemperatureRepository {
    void salvar(double temperatura);
    LocalDate buscarUltimaData();
}
