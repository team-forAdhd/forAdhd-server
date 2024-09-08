package com.project.foradhd.domain.hospital.web.dto.response;

import com.project.foradhd.domain.hospital.web.enums.HospitalOperationStatus;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalListNearbyResponse {

    private List<HospitalNearbyResponse> hospitalList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class HospitalNearbyResponse {

        private String hospitalId;
        private String name;
        private Integer totalReceiptReviewCount;
        private Integer totalEvaluationReviewCount;
        private Double latitude;
        private Double longitude;
        private Double distance;
        private HospitalOperationStatus operationStatus;
        private Boolean isBookmarked;

        public void synchronizeOperationStatus(HospitalOperationStatus operationStatus) {
            this.operationStatus = operationStatus;
        }
    }
}
