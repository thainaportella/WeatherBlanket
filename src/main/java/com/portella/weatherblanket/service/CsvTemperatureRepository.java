package com.portella.weatherblanket.service;

import com.portella.weatherblanket.DTO.ColorsEnum;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Repository
public class CsvTemperatureRepository implements TemperatureRepository {

    private static final String CSV_PATH =
            "C:\\Users\\thain\\Repository\\WeatherBlanket\\src\\main\\resources\\data\\temperaturas.csv";

    private static final ZoneId ZONA_BRASIL = ZoneId.of("America/Sao_Paulo");

    @Override
    public void salvar(double temperatura) {
        try {
            Path path = Paths.get(CSV_PATH);
            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(
                    path,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) {

                ColorsEnum cor = ColorsEnum.fromTemperatura(temperatura);

                writer.write(
                        LocalDate.now(ZONA_BRASIL) + "," +
                                LocalTime.now(ZONA_BRASIL)
                                        .format(DateTimeFormatter.ofPattern("HH:mm")) + "," +
                                temperatura + "," +
                                cor.name() + "," +
                                cor.getNomeOficial()
                );
                writer.newLine();
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar temperatura", e);
        }
    }

    @Override
    public LocalDate buscarUltimaData() {
        Path path = Paths.get(CSV_PATH);

        if (!Files.exists(path)) return null;

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String linha;
            String ultimaLinha = null;

            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    ultimaLinha = linha;
                }
            }

            if (ultimaLinha == null || ultimaLinha.startsWith("data")) {
                return null;
            }

            String data = ultimaLinha.split(",")[0];
            return LocalDate.parse(data);

        } catch (Exception e) {
            return null;
        }
    }
}

