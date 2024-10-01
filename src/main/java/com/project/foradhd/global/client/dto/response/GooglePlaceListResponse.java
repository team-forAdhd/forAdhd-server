package com.project.foradhd.global.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GooglePlaceListResponse {

    private List<GooglePlaceResponse> places;

    @Getter
    public static class GooglePlaceResponse {

        private String id;
        private String formattedAddress;
        private DisplayName displayName;
    }

    @Getter
    public static class DisplayName {

        private String text;
        private String languageCode;
    }
}
