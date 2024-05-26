package com.project.foradhd.domain.hospital.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HospitalListNearbyRequest {

    private Double latitude;
    private Double longitude;
    private Integer radius;
}
