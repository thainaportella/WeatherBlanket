package com.portella.weatherblanket.service;

import com.portella.weatherblanket.exceptions.ServiceException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class NominatimClient {

    private final HttpConnectionFactory factory;

    public NominatimClient(HttpConnectionFactory factory) {
        this.factory = factory;
    }

    public String buscarEndereco(double latitude, double longitude) {

        try {
            String urlStr =
                    "https://nominatim.openstreetmap.org/reverse" +
                            "?lat=" + latitude +
                            "&lon=" + longitude +
                            "&format=json" +
                            "&addressdetails=1";

            URL url = new URL(urlStr);
            HttpURLConnection conn = factory.create(url);

            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "User-Agent",
                    "TemperatureBlanket/1.0"
            );

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        } catch (Exception e) {
            throw new ServiceException(
                    502,
                    "SEARCH_LOCATION_ERROR",
                    "Erro ao buscar localização no Nominatim"
            );
        }
    }
}
