package com.project.foradhd.domain.hospital.web.dto.response;

import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalListBookmarkResponse {

    private List<HospitalBookmarkResponse> hospitalList;

    @Getter
    @Builder
    public static class HospitalBookmarkResponse {

        private String hospitalId;
        private String name;
        private Integer totalReceiptReviewCount;
        private Integer totalEvaluationReviewCount;
        private Double distance;
        private HospitalOperationStatus operationStatus;
    }
}
