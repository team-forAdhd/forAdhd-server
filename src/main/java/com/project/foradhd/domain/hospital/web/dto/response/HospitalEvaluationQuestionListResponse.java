package com.project.foradhd.domain.hospital.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalEvaluationQuestionListResponse {

    private List<HospitalEvaluationQuestionResponse> hospitalEvaluationQuestionList;

    @Getter
    @Builder
    public static class HospitalEvaluationQuestionResponse {

        private Long hospitalEvaluationQuestionId;
        private Integer seq;
        private String question;
    }
}
