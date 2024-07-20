package com.project.foradhd.global.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.project.foradhd.global.util.TimeUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeToEpochSecondSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        long epochSecond = TimeUtil.toEpochSecond(value);
        jsonGenerator.writeNumber(epochSecond);
    }
}
