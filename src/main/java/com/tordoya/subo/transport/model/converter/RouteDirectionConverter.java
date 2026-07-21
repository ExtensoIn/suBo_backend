package com.tordoya.subo.transport.model.converter;

import com.tordoya.subo.transport.model.RouteDirection;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RouteDirectionConverter implements AttributeConverter<RouteDirection, String> {
    @Override
    public String convertToDatabaseColumn(RouteDirection attribute) {
        return attribute == null ? null : attribute.getDatabaseValue();
    }

    @Override
    public RouteDirection convertToEntityAttribute(String dbData) {
        return dbData == null ? null : RouteDirection.fromDatabaseValue(dbData);
    }
}