package com.portella.weatherblanket.service;

import com.portella.weatherblanket.Enum.ColorsEnum;
import com.portella.weatherblanket.config.LocationConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
@Service
public class TemperatureSchedulerService {
    private static final String CSV_PATH = "/home/weather/data/registros.csv";
    private static final ZoneId ZONA_BRASIL = ZoneId.of("America/Sao_Paulo");
    private static final int REGISTERING_HOUR = 15;
    private static LocalDate ultimaDataRegistrada;

    @PostConstruct
    public void init() {
        carregarUltimaDataRegistrada();
    }

    @Scheduled(fixedRate = 60_000) // a cada minuto
    public void registrarTemperatura() {
        try {
            LocalDateTime agora = LocalDateTime.now(ZONA_BRASIL);
            LocalDate hoje = agora.toLocalDate();

            if (ultimaDataRegistrada != null && ultimaDataRegistrada.equals(hoje)) {
                return;
            }

            if (agora.getHour() >= REGISTERING_HOUR) {
                double temperatura = buscarTemperaturaComRetry();

                if (temperatura == Double.MIN_VALUE) {
                    return;
                }

                salvarNoCsv(temperatura);
                ultimaDataRegistrada = hoje;
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro no scheduler.");
        }
    }

    private static void carregarUltimaDataRegistrada() {
        Path path = Paths.get(CSV_PATH);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha, ultimaLinha = null;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) ultimaLinha = linha;
            }

            if (ultimaLinha != null && !ultimaLinha.startsWith("data")) {
                ultimaDataRegistrada = LocalDate.parse(ultimaLinha.split(",")[0]);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro na consulta da última data registrada.");
        }
    }

    private static double buscarTemperatura() {
        try {
            double lat = LocationConfig.getLatitude();
            double lon = LocationConfig.getLongitude();

            URL url = new URL(
                    "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                            "&longitude=" + lon +
                            "&current=temperature_2m"
            );

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);

                var json = new com.fasterxml.jackson.databind.ObjectMapper()
                        .readTree(response.toString());

                return json.path("current").path("temperature_2m").asDouble();
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro no momento da consulta da temperatura.");
            return Double.MIN_VALUE;
        }
    }

    private static void salvarNoCsv(double temperatura) {
        try {
            Path path = Paths.get(CSV_PATH);
            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(
                    path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

                ColorsEnum cor = ColorsEnum.fromTemperatura(temperatura);

                writer.write(
                        LocalDate.now(ZONA_BRASIL) + "," +
                                LocalTime.now(ZONA_BRASIL).format(DateTimeFormatter.ofPattern("HH:mm")) + "," +
                                temperatura + "," +
                                cor.name() + "," +
                                cor.getNomeOficial()
                );
                writer.newLine();

                System.out.println("Temperatura de " + temperatura +
                        " registrada. Cor associada: " + cor.name());
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no registro da temperatura no arquivo CSV.");
        }
    }

    private static double buscarTemperaturaComRetry() {
        int tentativas = 3;
        int esperaSegundos = 5;

        for (int i = 1; i <= tentativas; i++) {
            double temp = buscarTemperatura();

            if (temp != Double.MIN_VALUE && temp > -50 && temp < 60) {
                return temp;
            }

            System.out.println(
                    "Tentativa " + i + " falhou ao obter temperatura. Valor: " + temp
            );

            if (i < tentativas) {
                try {
                    Thread.sleep(esperaSegundos * 1000L);
                } catch (InterruptedException ignored) {
                }
            }
        }

        System.out.println("Todas as tentativas falharam. Registro ignorado.");
        return Double.MIN_VALUE;
    }
}
