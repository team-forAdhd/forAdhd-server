package com.project.foradhd.domain.hospital.web.dto.response;

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
        private Double totalGrade;
        private Long totalReviewCount;
        private Double latitude;
        private Double longitude;
        private Double distance;
        private Boolean isBookmarked;
    }
}
