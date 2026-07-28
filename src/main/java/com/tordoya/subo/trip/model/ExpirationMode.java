package com.tordoya.subo.trip.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

public enum ExpirationMode {
    UNTIL_ARRIVAL("until_arrival"),
    FIXED_TIME("fixed_time");

    private final String value;

    ExpirationMode(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static ExpirationMode fromValue(String value) {
        return Arrays.stream(values())
                .filter(mode -> mode.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown expiration mode: " + value));
    }

    @Converter
    public static class JpaConverter implements AttributeConverter<ExpirationMode, String> {
        @Override
        public String convertToDatabaseColumn(ExpirationMode attribute) {
            return attribute == null ? null : attribute.value;
        }

        @Override
        public ExpirationMode convertToEntityAttribute(String value) {
            return value == null ? null : ExpirationMode.fromValue(value);
        }
    }
}