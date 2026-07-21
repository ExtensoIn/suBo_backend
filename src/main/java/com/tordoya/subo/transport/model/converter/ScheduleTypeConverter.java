package com.tordoya.subo.transport.model.converter;

import com.tordoya.subo.transport.model.ScheduleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ScheduleTypeConverter implements AttributeConverter<ScheduleType, String> {
    @Override
    public String convertToDatabaseColumn(ScheduleType attribute) {
        return attribute == null ? null : attribute.getDatabaseValue();
    }

    @Override
    public ScheduleType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ScheduleType.fromDatabaseValue(dbData);
    }
}