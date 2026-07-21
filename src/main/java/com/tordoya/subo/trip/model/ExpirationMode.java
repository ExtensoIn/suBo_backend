package com.tordoya.subo.trip.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExpirationMode {
    @JsonProperty("until_arrival")
    UNTIL_ARRIVAL("until_arrival"),

    @JsonProperty("fixed_time")
    FIXED_TIME("fixed_time");

    private final String value;

    ExpirationMode(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}