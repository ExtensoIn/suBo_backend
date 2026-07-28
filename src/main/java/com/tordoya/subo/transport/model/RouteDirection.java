package com.tordoya.subo.transport.model;

import lombok.Getter;

@Getter
public enum RouteDirection {
    OUTBOUND("outbound"),
    INBOUND("inbound"),
    CIRCULAR("circular"),
    VARIANT("variant");

    private final String databaseValue;

    RouteDirection(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public static RouteDirection fromDatabaseValue(String value) {
        for (RouteDirection direction : values()) {
            if (direction.databaseValue.equals(value)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Unknown route direction: " + value);
    }
}