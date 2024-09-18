package com.project.foradhd.global.client.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
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
