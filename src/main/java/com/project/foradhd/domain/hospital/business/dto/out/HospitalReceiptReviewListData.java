package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class HospitalReceiptReviewListData {

    private List<ReceiptReviewData> receiptReviewList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class ReceiptReviewData {

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
