package com.portella.weatherblanket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portella.weatherblanket.config.LocationConfig;
import com.portella.weatherblanket.exceptions.ServiceException;
import com.portella.weatherblanket.model.Localizacao;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final NominatimClient nominatimClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocationService(NominatimClient nominatimClient) {
        this.nominatimClient = nominatimClient;
    }

    public Localizacao buscarLocalizacao(double latitude, double longitude) {

        try {
            String json = nominatimClient.buscarEndereco(latitude, longitude);

            JsonNode root = objectMapper.readTree(json);
            JsonNode address = root.path("address");

            String cidade =
                    address.path("city").asText(
                            address.path("town").asText(
                                    address.path("village").asText("")
                            )
                    );

            String estado = address.path("state").asText("");
            String pais = address.path("country").asText("");

            Localizacao localizacao = new Localizacao(
                    latitude,
                    longitude,
                    cidade,
                    estado,
                    pais
            );

            LocationConfig.setLatitude(latitude);
            LocationConfig.setLongitude(longitude);
            LocationConfig.setCidade(cidade);
            LocationConfig.setEstado(estado);
            LocationConfig.setPais(pais);

            return localizacao;

        } catch (Exception e) {
            throw new ServiceException(
                    500,
                    "LOCATION_PARSE_ERROR",
                    "Erro ao processar localização"
            );
        }
    }
}
