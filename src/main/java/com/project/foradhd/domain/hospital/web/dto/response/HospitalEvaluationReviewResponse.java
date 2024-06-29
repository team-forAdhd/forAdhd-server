package com.project.foradhd.domain.hospital.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalEvaluationReviewResponse {

    private List<HospitalEvaluationAnswerResponse> hospitalEvaluationAnswerList;

    @Getter
    @Builder
    public static class HospitalEvaluationAnswerResponse {

        private Long hospitalEvaluationQuestionId;
        private Integer seq;
        private String question;
        private Boolean answer;
    }
}
