package com.project.foradhd.domain.hospital.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorDetailsResponse {

    private String name;
    private Double totalGrade;
    private Long totalReviewCount;
    private String profile;
    private BriefReviewResponse briefReview;

    @Getter
    @Builder
    public static class BriefReviewResponse {

        private Long totalReviewCount;
        private Double kindness;
        private Double adhdUnderstanding;
        private Double enoughMedicalTime;
    }
}
