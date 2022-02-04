package com.parkit.parkingsystem.constants;

public enum ParkingType {
    CAR("CAR"),
    BIKE("BIKE"),
    UNKNOWN("UNKNOWN");

    private String value;

    ParkingType(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
