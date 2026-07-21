package com.tordoya.subo.transport.model;

public enum RouteDirection {
    OUTBOUND("outbound"),
    INBOUND("inbound"),
    CIRCULAR("circular");

    private final String databaseValue;

    RouteDirection(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
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