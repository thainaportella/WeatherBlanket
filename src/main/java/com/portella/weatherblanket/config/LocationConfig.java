package com.portella.weatherblanket.config;

public class LocationConfig {
    //por padrão, agriões-teresópolis
    private static double latitude = -22.4209;
    private static double longitude = -42.9801;

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLatitude(double latitude) {
        LocationConfig.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        LocationConfig.longitude = longitude;
    }
}

