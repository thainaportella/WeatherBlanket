package com.portella.weatherblanket.model;

public class LocationInfo {

    private double latitude;
    private double longitude;
    private String cidade;
    private String estado;
    private String pais;

    public LocationInfo(double latitude, double longitude, String cidade, String estado, String pais) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getPais() {
        return pais;
    }
}
