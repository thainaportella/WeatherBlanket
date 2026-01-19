package com.portella.weatherblanket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portella.weatherblanket.model.LocationInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationService {

    public LocationInfo buscarLocalizacao(double latitude, double longitude) {

        try {
            String urlStr =
                    "https://nominatim.openstreetmap.org/reverse" +
                            "?lat=" + latitude +
                            "&lon=" + longitude +
                            "&format=json" +
                            "&addressdetails=1";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.setRequestProperty(
                    "User-Agent",
                    "WeatherBlanket/1.0 (contato@weatherblanket.local)"
            );

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());
            JsonNode address = root.path("address");

            String cidade =
                    address.path("city").asText(
                            address.path("town").asText(
                                    address.path("village").asText("")
                            )
                    );

            String estado = address.path("state").asText("");
            String pais = address.path("country").asText("");

            return new LocationInfo(
                    latitude,
                    longitude,
                    cidade,
                    estado,
                    pais
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
