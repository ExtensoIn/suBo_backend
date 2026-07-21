package com.tordoya.subo.transport.model;

import lombok.Getter;

@Getter
public enum TransportMode {
    TELEFERICO("teleferico"),
    PUMAKATARI("pumakatari"),
    MINIBUS("minibus"),
    TRUFI("trufi");

    private final String databaseValue;

    TransportMode(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static TransportMode fromDatabaseValue(String value) {
        for (TransportMode mode : values()) {
            if (mode.databaseValue.equals(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown transport mode: " + value);
    }
}