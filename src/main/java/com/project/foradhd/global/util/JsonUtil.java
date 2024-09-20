package com.project.foradhd.global.util;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.foradhd.global.exception.InternalSystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static <T> T readValue(String content, Class<T> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            throw new InternalSystemException(e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new InternalSystemException(e);
        }
    }

    public static <T> T readValue(InputStream stream, Class<T> clazz) {
        try {
            return objectMapper.readValue(stream, clazz);
        } catch (IOException e) {
            throw new InternalSystemException(e);
        }
    }

    public static <T> String writeValueAsString(T value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new InternalSystemException(e);
        }
    }
}
