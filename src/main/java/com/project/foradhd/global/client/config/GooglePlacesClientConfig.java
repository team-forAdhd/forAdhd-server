package com.project.foradhd.global.client.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GooglePlacesClientConfig {

    private static final String HEADER_SEPARATOR = ",";
    private static final String GOOGLE_PLACES_OPERATION_DETAILS_FIELD_MASK_HEADER = "currentOpeningHours";
    private static final String GOOGLE_PLACES_SEARCH_FIELD_MASK_HEADER = String.join(HEADER_SEPARATOR, List.of("places.displayName", "places.formattedAddress", "places.id"));
    private static final String GOOGLE_LANGUAGE_CODE_PARAM = "ko";
    private static final String PLACES_SEARCH_PATH = "searchText";

    @Value("${google.places.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor googlePlacesRequestInterceptor() {
        return restTemplate -> {
            restTemplate.header("X-Goog-Api-Key", apiKey);
            restTemplate.query("languageCode", GOOGLE_LANGUAGE_CODE_PARAM);
            if (restTemplate.url().contains(PLACES_SEARCH_PATH)) {
                restTemplate.header("X-Goog-FieldMask", GOOGLE_PLACES_SEARCH_FIELD_MASK_HEADER);
            } else {
                restTemplate.header("X-Goog-FieldMask", GOOGLE_PLACES_OPERATION_DETAILS_FIELD_MASK_HEADER);
            }
        };
    }
}
