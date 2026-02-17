package com.portella.weatherblanket.repositories;

import com.portella.weatherblanket.entities.DTOs.RegistroDTO;
import com.portella.weatherblanket.entities.TemperatureEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TemperatureRepository {
    void persistData(double temp);
    LocalDate getLastRecordedDate();
    List<RegistroDTO> listRecords(Integer month, Integer year, String order, Integer limit);
    Optional<TemperatureEntity> getRecordByDate(LocalDate date);
    void updateStatus(TemperatureEntity record);

}
