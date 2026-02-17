package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.entities.LocationEntity;
import com.portella.weatherblanket.model.LocationRequest;
import com.portella.weatherblanket.model.LocationResponse;
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


    @GetMapping("/location")
    public LocationResponse getCurrentLocation() {
        return locationService.getLocation(
                LocationEntity.getLatitude(),
                LocationEntity.getLongitude()
        );
    }

    @PutMapping("/location")
    public LocationResponse updateLocation(@RequestBody LocationRequest request) {

        double latitude = request.getLatitude();
        double longitude = request.getLongitude();

        LocationEntity.setLatitude(latitude);
        LocationEntity.setLongitude(longitude);

        LocationResponse location = locationService.getLocation(latitude, longitude);

        LocationEntity.setCity(location.getCity());
        LocationEntity.setState(location.getState());
        LocationEntity.setCountry(location.getCountry());

        System.out.println("Location was changed to: " + location.getCity());

        return location;
    }

    @PutMapping("/location/reset")
    public LocationResponse resetLocation() {

        LocationEntity.setLatitude(-22.4209);
        LocationEntity.setLongitude(-42.9801);

        return locationService.getLocation(
                LocationEntity.getLatitude(),
                LocationEntity.getLongitude()
        );

    }

}