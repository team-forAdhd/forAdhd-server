package com.project.foradhd.domain.hospital.business.dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorDetailsData {

    private String name;
    private Double totalGrade;
    private Long totalReviewCount;
    private String profile;
    private BriefReviewData briefReview;

    @Getter
    @Builder
    public static class BriefReviewData {

        private Long totalReviewCount;
        private Double kindness;
        private Double adhdUnderstanding;
        private Double enoughMedicalTime;
    }
}
