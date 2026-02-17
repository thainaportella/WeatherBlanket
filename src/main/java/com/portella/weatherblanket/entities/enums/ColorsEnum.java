package com.portella.weatherblanket.entities.enums;

import com.portella.weatherblanket.exceptions.ServiceException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum ColorsEnum {

    PURPLE(null, 13.9, "ametista violeta"),
    INDIGO(14.0, 16.9, "anil profundo"),
    BLUE(17.0, 19.9, "azul candy"),
    GREEN(20.0, 22.9, "musgo verde"),
    YELLOW(23.0, 25.9, "solar"),
    MUSTARD(26.0, 28.9, "mostarda"),
    ORANGE(29.0, 31.9, "brasa"),
    RED(32.0, 34.9, "paixão"),
    BURGUNDY(35.0, null, "devoção");


    private final Double minTemp;
    private final Double maxTemp;
    private final String yarnColor;

    ColorsEnum(Double minTemp, Double maxTemp, String nomeOficial) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.yarnColor = nomeOficial;
    }

    public String getYarnColor() {
        return yarnColor;
    }

    private boolean matchTemperatureAndColor(double temperature) {
        if (minTemp == null) {
            return temperature <= maxTemp;
        }
        if (maxTemp == null) {
            return temperature >= minTemp;
        }
        return temperature >= minTemp && temperature <= maxTemp;
    }

    /**
     * Retorna a color correspondente à temperature (com arredondamento seguro)
     */
    public static ColorsEnum fromTemperature(double temperature) {

        double tempNormalized = BigDecimal
                .valueOf(temperature)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();

        for (ColorsEnum color : values()) {
            if (color.matchTemperatureAndColor(tempNormalized)) {
                return color;
            }
        }

        throw new ServiceException(500, "TEMPERATURE_RANGE_ERROR", "Temperature out of configured range: " + tempNormalized);
    }
}

