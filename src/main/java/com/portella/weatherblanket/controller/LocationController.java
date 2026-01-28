package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.config.LocationConfig;
import com.portella.weatherblanket.model.Localizacao;
import com.portella.weatherblanket.model.LocalizacaoRequest;
import com.portella.weatherblanket.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping("/localizacao")
    public Localizacao getLocalizacaoAtual() {
        return locationService.buscarLocalizacao(
                LocationConfig.getLatitude(),
                LocationConfig.getLongitude()
        );
    }

    @PutMapping("/localizacao")
    public Localizacao atualizar(@RequestBody LocalizacaoRequest request) {

        double latitude = request.getLatitude();
        double longitude = request.getLongitude();

        LocationConfig.setLatitude(latitude);
        LocationConfig.setLongitude(longitude);

        Localizacao localizacao = locationService.buscarLocalizacao(latitude, longitude);

        LocationConfig.setCidade(localizacao.getCidade());
        LocationConfig.setEstado(localizacao.getEstado());
        LocationConfig.setPais(localizacao.getPais());

        System.out.println("Foi alterada a localização para a cidade: " + localizacao.getCidade());

        return localizacao;
    }

    @PutMapping("/localizacao/reset")
    public Localizacao resetarLocalizacao() {

        LocationConfig.setLatitude(-22.4209);
        LocationConfig.setLongitude(-42.9801);

        return locationService.buscarLocalizacao(
                LocationConfig.getLatitude(),
                LocationConfig.getLongitude()
        );

    }

}