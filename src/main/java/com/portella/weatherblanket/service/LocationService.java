package com.portella.weatherblanket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portella.weatherblanket.entities.LocationEntity;
import com.portella.weatherblanket.exceptions.ServiceException;
import com.portella.weatherblanket.model.LocationResponse;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final NominatimClientService nominatimClientService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocationService(NominatimClientService nominatimClientService) {
        this.nominatimClientService = nominatimClientService;
    }

    public LocationResponse getLocation(double latitude, double longitude) {

        try {
            String json = nominatimClientService.getAddress(latitude, longitude);

            JsonNode root = objectMapper.readTree(json);
            JsonNode address = root.path("address");

            String city =
                    address.path("city").asText(
                            address.path("town").asText(
                                    address.path("village").asText("")
                            )
                    );

            String state = address.path("state").asText("");
            String country = address.path("country").asText("");

            LocationResponse location = new LocationResponse(
                    latitude,
                    longitude,
                    city,
                    state,
                    country
            );

            LocationEntity.setLatitude(latitude);
            LocationEntity.setLongitude(longitude);
            LocationEntity.setCity(city);
            LocationEntity.setState(state);
            LocationEntity.setCountry(country);

            return location;

        } catch (Exception e) {
            throw new ServiceException(
                    500,
                    "LOCATION_PARSE_ERROR",
                    "Error processing location"
            );
        }
    }
}
