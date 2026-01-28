package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.exceptions.ServiceException;
import com.portella.weatherblanket.model.RegistroTemperatura;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RegistrosController {

    //private static final String CSV_PATH = "/home/weather/data/registros.csv";
    private static final String CSV_PATH = "C:\\Users\\thain\\Repository\\WeatherBlanket\\src\\main\\resources\\data\\temperaturas.csv";

    @GetMapping("/registros")
    public List<RegistroTemperatura> listarRegistros(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false) Integer limit
    ) throws Exception {

        List<String> linhas = Files.readAllLines(Path.of(CSV_PATH));
        List<RegistroTemperatura> registros = new ArrayList<>();

        LocalDate limite = null;

        for (int i = 1; i < linhas.size(); i++) {
            String[] partes = linhas.get(i).split(",");

            LocalDate dataRegistro = LocalDate.parse(partes[0]);

            if (limite != null && dataRegistro.isBefore(limite)) {
                continue;
            }

            if (mes != null && ano == null) {
                throw new ServiceException(400, "BAD_REQUEST", "O parâmetro 'mes' deve ser informado junto de 'ano'.");
            }

            if (mes != null && ano != null) {
                if (dataRegistro.getMonthValue() != mes || dataRegistro.getYear() != ano) {
                    continue;
                }
            }

            registros.add(new RegistroTemperatura(
                    partes[0],
                    partes[1],
                    Double.parseDouble(partes[2]),
                    partes[3],
                    partes[4]
            ));
        }

        registros.sort((a, b) -> {
            LocalDate da = LocalDate.parse(a.getData());
            LocalDate db = LocalDate.parse(b.getData());

            return "asc".equalsIgnoreCase(order)
                    ? da.compareTo(db)
                    : db.compareTo(da);
        });


        if (limit != null && limit > 0 && limit < registros.size()) {
            registros = registros.subList(0, limit);
        }

        return registros;
    }




}
