package com.project.foradhd.global.client;

import com.project.foradhd.global.client.config.GooglePlacesClientConfig;
import com.project.foradhd.global.client.dto.response.GooglePlaceListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googlePlacesClient", url = "${google.places.url}",
        configuration = GooglePlacesClientConfig.class)
public interface GooglePlacesClient {

    @PostMapping(":searchText")
    GooglePlaceListResponse searchPlaces(@RequestParam String textQuery);
}
