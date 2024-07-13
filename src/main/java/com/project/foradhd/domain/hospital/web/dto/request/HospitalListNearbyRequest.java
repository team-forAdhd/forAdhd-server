package com.project.foradhd.domain.hospital.web.dto.request;

import com.project.foradhd.domain.hospital.web.enums.HospitalFilter;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HospitalListNearbyRequest {

    private Double latitude;
    private Double longitude;
    private Integer radius;
    private HospitalFilter filter;
}
