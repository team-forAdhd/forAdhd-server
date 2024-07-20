package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.Hospital;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalListNearbyData {

    private List<HospitalNearbyData> hospitalList;
    private PagingResponse paging;

    @Getter
    @Builder
    public static class HospitalNearbyData {

        private Hospital hospital;
        private Double distance;
        private Boolean isBookmarked;
    }
}
