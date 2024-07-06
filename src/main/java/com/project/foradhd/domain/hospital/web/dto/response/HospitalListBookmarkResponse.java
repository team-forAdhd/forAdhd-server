package com.project.foradhd.domain.hospital.web.dto.response;

import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalListBookmarkResponse {

    private List<HospitalBookmarkResponse> hospitalList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class HospitalBookmarkResponse {

        private String hospitalId;
        private String name;
        private Integer totalReceiptReviewCount;
        private Integer totalEvaluationReviewCount;
        private HospitalOperationStatus operationStatus;
    }
}
