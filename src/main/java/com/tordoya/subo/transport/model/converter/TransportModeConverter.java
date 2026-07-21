package com.tordoya.subo.transport.model.converter;

import com.tordoya.subo.transport.model.TransportMode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransportModeConverter implements AttributeConverter<TransportMode, String> {
    @Override
    public String convertToDatabaseColumn(TransportMode attribute) {
        return attribute == null ? null : attribute.getDatabaseValue();
    }

    @Override
    public TransportMode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TransportMode.fromDatabaseValue(dbData);
    }
}