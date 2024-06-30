package com.project.foradhd.domain.hospital.business.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalListBookmarkSearchCond {

    private Double latitude;
    private Double longitude;
}
