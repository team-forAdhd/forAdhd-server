package com.project.foradhd.domain.hospital.web.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class HospitalEvaluationReviewUpdateRequest {

    private List<HospitalEvaluationAnswerUpdateRequest> hospitalEvaluationAnswerList;

    @Getter
    public static class HospitalEvaluationAnswerUpdateRequest {

        private Long hospitalEvaluationQuestionId;
        private Boolean answer;
    }
}
