package com.tordoya.subo.trip.model.converter;

import com.tordoya.subo.trip.model.TripMode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TripModeConverter implements AttributeConverter<TripMode, String> {

    @Override
    public String convertToDatabaseColumn(TripMode mode) {
        return mode == null ? null : mode.value();
    }

    @Override
    public TripMode convertToEntityAttribute(String value) {
        return value == null ? null : TripMode.fromValue(value);
    }
}