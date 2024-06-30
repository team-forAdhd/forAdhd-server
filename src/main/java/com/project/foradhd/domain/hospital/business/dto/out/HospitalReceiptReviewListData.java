package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalReceiptReview;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalReceiptReviewListData {

    private List<HospitalReceiptReviewData> hospitalReceiptReviewList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class HospitalReceiptReviewData {

        private HospitalReceiptReview hospitalReceiptReview;
        private String writerId;
        private String writerName;
        private String writerImage;
        private String doctorName;
        private Boolean isHelped;
        private Boolean isMine;
    }
}
