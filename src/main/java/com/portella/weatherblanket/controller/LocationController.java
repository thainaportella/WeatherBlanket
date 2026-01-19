package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.config.LocationConfig;
import com.portella.weatherblanket.model.LocationInfo;
import com.portella.weatherblanket.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    private final LocationService locationService = new LocationService();


    @GetMapping("/localizacao")
    public LocationInfo getLocalizacaoAtual() {
        return locationService.buscarLocalizacao(
                LocationConfig.getLatitude(),
                LocationConfig.getLongitude()
        );
    }

    @GetMapping("/localizacao/set")
    public String alterarLocalizacao(
            @RequestParam String lat,
            @RequestParam String lon
    ) {
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);

        LocationConfig.setLatitude(latitude);
        LocationConfig.setLongitude(longitude);

        return "Localização atualizada para latitude=" + latitude + " longitude=" + longitude;
    }

    @GetMapping("/localizacao/reset")
    public String resetarLocalizacao() {

        LocationConfig.setLatitude(-22.4209);
        LocationConfig.setLongitude(-42.9801);

        return "Localização resetada para configuração base (API padrão)";
    }

}