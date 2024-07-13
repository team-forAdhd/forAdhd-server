package com.project.foradhd.domain.hospital.business.dto.in;

import com.project.foradhd.domain.hospital.web.enums.HospitalFilter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalListNearbySearchCond {

    private Double latitude;
    private Double longitude;
    private Integer radius;
    private HospitalFilter filter;
}
