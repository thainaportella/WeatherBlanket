package com.portella.weatherblanket.entities;

import com.portella.weatherblanket.exceptions.ServiceException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum ColorsEnum {

    ROXO(null, 13.9, "ametista violeta"),
    ANIL(14.0, 16.9, "anil profundo"),
    AZUL(17.0, 19.9, "azul candy"),
    VERDE(20.0, 22.9, "musgo verde"),
    AMARELO(23.0, 25.9, "solar"),
    MOSTARDA(26.0, 28.9, "mostarda"),
    LARANJA(29.0, 31.9, "brasa"),
    VERMELHO(32.0, 34.9, "paixão"),
    VINHO(35.0, null, "devoção");

    private final Double minTemp;
    private final Double maxTemp;
    private final String nomeOficial;

    ColorsEnum(Double minTemp, Double maxTemp, String nomeOficial) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.nomeOficial = nomeOficial;
    }

    public String getNomeOficial() {
        return nomeOficial;
    }

    /**
     * Verifica se a temperatura pertence à faixa da cor
     */
    private boolean corresponde(double temperatura) {
        if (minTemp == null) {
            return temperatura <= maxTemp;
        }
        if (maxTemp == null) {
            return temperatura >= minTemp;
        }
        return temperatura >= minTemp && temperatura <= maxTemp;
    }

    /**
     * Retorna a cor correspondente à temperatura (com arredondamento seguro)
     */
    public static ColorsEnum fromTemperatura(double temperatura) {

        double tempNormalizada = BigDecimal
                .valueOf(temperatura)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();

        for (ColorsEnum cor : values()) {
            if (cor.corresponde(tempNormalizada)) {
                return cor;
            }
        }

        throw new ServiceException(500, "TEMPERATURE_RANGE_ERROR", "Temperatura fora das faixas configuradas: " + tempNormalizada);
    }
}

