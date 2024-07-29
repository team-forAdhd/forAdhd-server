package com.project.foradhd.global.client;

import com.project.foradhd.global.client.config.GooglePlacesClientConfig;
import com.project.foradhd.global.client.dto.response.GooglePlaceOpeningHoursResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "googlePlacesClient", url = "${google.places.url}",
        configuration = GooglePlacesClientConfig.class)
public interface GooglePlacesClient {

    @GetMapping("/{placeId}")
    GooglePlaceOpeningHoursResponse getPlaceOpeningHours(@PathVariable String placeId);
}
