package com.parkit.parkingsystem.constants;

public enum ParkingType {
    CAR("CAR"),
    BIKE("BIKE"),
    UNKNOWN("UNKNOWN");

    private final String value;

    ParkingType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
