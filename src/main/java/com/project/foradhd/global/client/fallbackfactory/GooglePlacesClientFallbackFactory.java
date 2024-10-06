package com.project.foradhd.global.client.fallbackfactory;

import com.project.foradhd.global.client.GooglePlacesClient;
import com.project.foradhd.global.client.dto.response.GooglePlaceListResponse;
import com.project.foradhd.global.client.dto.response.GooglePlaceOpeningHoursResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class GooglePlacesClientFallbackFactory implements FallbackFactory<GooglePlacesClient> {

    @Override
    public GooglePlacesClient create(Throwable cause) {
        log.error("An exception occurred when calling the GooglePlacesClient", cause);

        //Google Places API 호출 시 에러 발생하면 기본 응답값을 반환하는 client 정의
        return new GooglePlacesClient() {

            @Override
            public GooglePlaceListResponse searchPlaces(String textQuery) {
                return new GooglePlaceListResponse(List.of());
            }

            @Override
            public Optional<GooglePlaceOpeningHoursResponse> getPlaceOpeningHours(String placeId) {
                return Optional.empty();
            }
        };
    }
}
