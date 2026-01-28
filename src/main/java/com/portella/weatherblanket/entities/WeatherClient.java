package com.portella.weatherblanket.entities;

public interface WeatherClient {
    double buscarTemperatura(double lat, double lon);
}
