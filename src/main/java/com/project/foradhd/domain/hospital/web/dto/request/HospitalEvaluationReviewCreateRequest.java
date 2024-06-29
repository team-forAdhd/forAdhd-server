package com.project.foradhd.domain.hospital.web.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class HospitalEvaluationReviewCreateRequest {

    private List<HospitalEvaluationAnswerCreateRequest> hospitalEvaluationAnswerList;

    @Getter
    public static class HospitalEvaluationAnswerCreateRequest {

        private Long hospitalEvaluationQuestionId;
        private Boolean answer;
    }
}
