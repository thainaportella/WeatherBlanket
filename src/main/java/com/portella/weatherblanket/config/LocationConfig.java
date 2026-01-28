package com.portella.weatherblanket.config;

public class LocationConfig {

    // padrão: Agriões – Teresópolis
    private static double latitude = -22.4209;
    private static double longitude = -42.9801;

    private static String cidade = "Teresópolis";
    private static String estado = "RJ";
    private static String pais = "Brasil";

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static String getCidade() {
        return cidade;
    }

    public static String getEstado() {
        return estado;
    }

    public static String getPais() {
        return pais;
    }

    public static void setLatitude(double latitude) {
        LocationConfig.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        LocationConfig.longitude = longitude;
    }

    public static void setCidade(String cidade) {
        LocationConfig.cidade = cidade;
    }

    public static void setEstado(String estado) {
        LocationConfig.estado = estado;
    }

    public static void setPais(String pais) {
        LocationConfig.pais = pais;
    }
}

