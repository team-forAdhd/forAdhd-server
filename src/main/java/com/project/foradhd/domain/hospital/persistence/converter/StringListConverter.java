package com.project.foradhd.domain.hospital.persistence.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

import static com.project.foradhd.global.util.JsonUtil.readValue;
import static com.project.foradhd.global.util.JsonUtil.writeValueAsString;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return writeValueAsString(attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String column) {
        return readValue(column, new TypeReference<>() {});
    }
}
