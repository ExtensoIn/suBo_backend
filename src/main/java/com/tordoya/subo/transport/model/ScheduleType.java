package com.tordoya.subo.transport.model;

public enum ScheduleType {
    FIXED("fixed"),
    FREQUENCY_BASED("frequency_based"),
    ESTIMATED_FREQUENCY("estimated_frequency");

    private final String databaseValue;

    ScheduleType(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public static ScheduleType fromDatabaseValue(String value) {
        for (ScheduleType type : values()) {
            if (type.databaseValue.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown schedule type: " + value);
    }
}