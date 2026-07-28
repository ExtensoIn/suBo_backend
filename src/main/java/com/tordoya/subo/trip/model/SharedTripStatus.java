package com.tordoya.subo.trip.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

public enum SharedTripStatus {
    IN_PROGRESS("en_curso"),
    FINISHED("finalizado"),
    CANCELLED("cancelado");

    private final String value;

    SharedTripStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static SharedTripStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(status -> status.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown shared trip status: " + value));
    }

    @Converter
    public static class JpaConverter implements AttributeConverter<SharedTripStatus, String> {

        @Override
        public String convertToDatabaseColumn(SharedTripStatus attribute) {
            return attribute == null ? null : attribute.value;
        }

        @Override
        public SharedTripStatus convertToEntityAttribute(String value) {
            return value == null ? null : SharedTripStatus.fromValue(value);
        }
    }
}