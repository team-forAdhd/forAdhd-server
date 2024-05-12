package com.project.foradhd.domain.hospital.persistence.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalBriefReviewSummary {

    private Long totalBriefReviewCount;
    private Long totalKindnessSum;
    private Long totalAdhdUnderstandingSum;
    private Long totalEnoughMedicalTimeSum;
}
