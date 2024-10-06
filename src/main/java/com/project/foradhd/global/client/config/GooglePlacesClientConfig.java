package com.project.foradhd.global.client.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class GooglePlacesClientConfig {

    private static final String HEADER_SEPARATOR = ",";
    private static final String GOOGLE_PLACES_OPERATION_DETAILS_FIELD_MASK_HEADER = "currentOpeningHours";
    private static final String GOOGLE_PLACES_SEARCH_FIELD_MASK_HEADER = String.join(HEADER_SEPARATOR, List.of("places.displayName", "places.formattedAddress", "places.id"));
    private static final String GOOGLE_LANGUAGE_CODE_PARAM = "ko";
    private static final String PLACES_SEARCH_PATH = "searchText";

    @Value("${google.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor googlePlacesClientRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Goog-Api-Key", apiKey);
            requestTemplate.query("languageCode", GOOGLE_LANGUAGE_CODE_PARAM);
            if (requestTemplate.url().contains(PLACES_SEARCH_PATH)) {
                requestTemplate.header("X-Goog-FieldMask", GOOGLE_PLACES_SEARCH_FIELD_MASK_HEADER);
            } else {
                requestTemplate.header("X-Goog-FieldMask", GOOGLE_PLACES_OPERATION_DETAILS_FIELD_MASK_HEADER);
            }
        };
    }
}
