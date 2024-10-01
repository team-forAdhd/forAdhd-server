package com.project.foradhd.global.client;

import com.project.foradhd.global.client.config.GooglePlacesClientConfig;
import com.project.foradhd.global.client.dto.response.GooglePlaceListResponse;
import com.project.foradhd.global.client.dto.response.GooglePlaceOpeningHoursResponse;
import com.project.foradhd.global.client.fallbackfactory.GooglePlacesClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "googlePlacesClient", url = "${google.places.url}",
        configuration = GooglePlacesClientConfig.class, fallbackFactory = GooglePlacesClientFallbackFactory.class)
public interface GooglePlacesClient {

    @PostMapping(":searchText")
    GooglePlaceListResponse searchPlaces(@RequestParam String textQuery);

    @GetMapping("/{placeId}")
    Optional<GooglePlaceOpeningHoursResponse> getPlaceOpeningHours(@PathVariable String placeId);
}
