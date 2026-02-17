package com.portella.weatherblanket.entities;

public interface WeatherClient {
    double getTemperature(double lat, double lon);
}
