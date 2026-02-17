package com.portella.weatherblanket.entities;

public class LocationEntity {

    // base location: Agriões – Teresópolis
    private static double latitude = -22.4209;
    private static double longitude = -42.9801;

    private static String city = "Teresópolis";
    private static String state = "RJ";
    private static String country = "Brasil";

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static String getCity() {
        return city;
    }

    public static String getState() {
        return state;
    }

    public static String getCountry() {
        return country;
    }

    public static void setLatitude(double latitude) {
        LocationEntity.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        LocationEntity.longitude = longitude;
    }

    public static void setCity(String city) {
        LocationEntity.city = city;
    }

    public static void setState(String state) {
        LocationEntity.state = state;
    }

    public static void setCountry(String country) {
        LocationEntity.country = country;
    }
}

