package com.project.foradhd.domain.hospital.web.dto.response;

import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class HospitalReceiptReviewListResponse {

    private List<ReceiptReviewResponse> receiptReviewList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class ReceiptReviewResponse {

        private String writerId;
        private String name;
        private String image;
        private Double totalGrade;
        private LocalDateTime createdAt;
        private List<String> reviewImageList;
        private String content;
        private Integer helpCount;
        private Boolean isHelped;
        private Boolean isMine;
    }
}
