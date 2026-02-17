package com.portella.weatherblanket.entities.enums;
public enum DayStatusEnum {

    PENDING("PENDING"),
    DONE("DONE"),
    LATE("LATE");

    private final String value;

    DayStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String value) {
        for (DayStatusEnum status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}


