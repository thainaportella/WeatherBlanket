package com.portella.weatherblanket.repositories;

import com.portella.weatherblanket.entities.RegistroDTO;
import java.util.List;
import java.time.LocalDate;

public interface TemperatureRepository {
    void salvar(double temperatura);
    LocalDate buscarUltimaData();
    List<RegistroDTO> listarRegistros(Integer mes, Integer ano, String order, Integer limit);
}
