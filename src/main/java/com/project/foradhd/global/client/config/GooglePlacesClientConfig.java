package com.project.foradhd.global.client.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GooglePlacesClientConfig {

    private static final String GOOGLE_OPERATION_STATUS_FIELD_MASK_HEADER = "currentOpeningHours";
    private static final String GOOGLE_LANGUAGE_CODE_PARAM = "ko";

    @Value("${google.places.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor googlePlacesRequestInterceptor() {
        return restTemplate -> {
            restTemplate.header("X-Goog-Api-Key", apiKey);
            restTemplate.header("X-Goog-FieldMask", GOOGLE_OPERATION_STATUS_FIELD_MASK_HEADER);
            restTemplate.query("languageCode", GOOGLE_LANGUAGE_CODE_PARAM);
        };
    }
}
