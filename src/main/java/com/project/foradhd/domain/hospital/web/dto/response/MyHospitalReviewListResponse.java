package com.project.foradhd.domain.hospital.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.hospital.web.enums.HospitalReviewType;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyHospitalReviewListResponse {

    private List<MyHospitalReviewResponse> hospitalReviewList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class MyHospitalReviewResponse {

        private String hospitalReviewId;
        private String hospitalId;
        private String hospitalName;
        private HospitalReviewType reviewType;
        @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
        private LocalDateTime createdAt;
        private String content;
        private List<String> imageList;
    }
}
