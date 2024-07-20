package com.project.foradhd.domain.hospital.web.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class HospitalReceiptReviewListResponse {

    private List<HospitalReceiptReviewResponse> hospitalReceiptReviewList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class HospitalReceiptReviewResponse {

        private String hospitalReceiptReviewId;
        private String writerId;
        private String writerName;
        private String writerImage;
        private String doctorName;
        @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
        private LocalDateTime createdAt;
        private String content;
        private List<String> imageList;
        private Long medicalExpense;
        private Integer helpCount;
        private Boolean isHelped;
        private Boolean isMine;
    }
}
