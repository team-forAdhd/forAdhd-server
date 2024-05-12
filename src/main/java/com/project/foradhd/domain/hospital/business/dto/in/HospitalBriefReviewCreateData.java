package com.project.foradhd.domain.hospital.business.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalBriefReviewCreateData {

    private Integer kindness;
    private Integer adhdUnderstanding;
    private Integer enoughMedicalTime;
}
