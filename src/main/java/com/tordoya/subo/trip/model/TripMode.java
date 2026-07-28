package com.tordoya.subo.trip.model;

import com.tordoya.subo.routing.model.ModoTransporte;

import java.util.Arrays;

public enum TripMode {
    WALK("caminar"),
    TELEFERICO("teleferico"),
    PUMAKATARI("pumakatari"),
    MINIBUS("minibus"),
    TRUFI("trufi");

    private final String value;

    TripMode(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static TripMode fromRoutingMode(ModoTransporte mode) {
        return switch (mode) {
            case CAMINAR -> WALK;
            case TELEFERICO -> TELEFERICO;
            case PUMAKATARI -> PUMAKATARI;
            case MINIBUS -> MINIBUS;
            case TRUFI -> TRUFI;
        };
    }

    public static TripMode fromValue(String value) {
        return Arrays.stream(values())
                .filter(mode -> mode.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown trip mode: " + value));
    }
}