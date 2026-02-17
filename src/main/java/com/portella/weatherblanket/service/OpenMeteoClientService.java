package com.portella.weatherblanket.service;

import com.portella.weatherblanket.entities.WeatherClient;
import com.portella.weatherblanket.factory.HttpConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class OpenMeteoClientService implements WeatherClient {

    private final HttpConnectionFactory factory;

    public OpenMeteoClientService(HttpConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public double getTemperature(double lat, double lon) {

        try {
            URL url = new URL(
                    "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                            "&longitude=" + lon +
                            "&current=temperature_2m"
            );

            HttpURLConnection conn = factory.create(url);
            conn.setRequestMethod("GET");

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                var json = new com.fasterxml.jackson.databind.ObjectMapper()
                        .readTree(response.toString());

                return json.path("current").path("temperature_2m").asDouble();
            }

        } catch (Exception e) {
            return Double.MIN_VALUE;
        }
    }
}
