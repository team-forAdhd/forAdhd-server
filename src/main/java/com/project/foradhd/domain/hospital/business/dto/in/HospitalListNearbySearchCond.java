package com.project.foradhd.domain.hospital.business.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalListNearbySearchCond {

    private Double latitude;
    private Double longitude;
    private Integer radius;
}
