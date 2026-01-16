package com.portella.weatherblanket.controller;

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

    private static final String CSV_PATH = "/home/weather/data/registros.csv";

    @GetMapping("/registros")
    public List<RegistroDTO> listarRegistros(
            @RequestParam(required = false) String data,
            @RequestParam(required = false) Integer ultimosDias,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false) Integer limit
    ) throws Exception {

        List<String> linhas = Files.readAllLines(Path.of(CSV_PATH));
        List<RegistroDTO> registros = new ArrayList<>();

        LocalDate hoje = LocalDate.now();
        LocalDate limite = null;

        if (ultimosDias != null) {
            limite = hoje.minusDays(ultimosDias);
        }

        for (int i = 1; i < linhas.size(); i++) {
            String[] partes = linhas.get(i).split(",");

            LocalDate dataRegistro = LocalDate.parse(partes[0]);

            if (data != null && !dataRegistro.equals(LocalDate.parse(data))) {
                continue;
            }

            if (limite != null && dataRegistro.isBefore(limite)) {
                continue;
            }

            if (mes != null && ano != null) {
                if (dataRegistro.getMonthValue() != mes || dataRegistro.getYear() != ano) {
                    continue;
                }
            }

            registros.add(new RegistroDTO(
                    partes[0],
                    partes[1],
                    partes[2],
                    partes[3],
                    partes[4]
            ));
        }

        // 🔹 ORDENAÇÃO POR DATA
        registros.sort((a, b) -> {
            LocalDate da = LocalDate.parse(a.data());
            LocalDate db = LocalDate.parse(b.data());
            return "asc".equalsIgnoreCase(order)
                    ? da.compareTo(db)
                    : db.compareTo(da);
        });

        // 🔹 LIMITE
        if (limit != null && limit > 0 && limit < registros.size()) {
            registros = registros.subList(0, limit);
        }

        return registros;
    }




}
