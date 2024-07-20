package com.project.foradhd.domain.hospital.business.dto.in;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalEvaluationReviewCreateData {

    private List<HospitalEvaluationAnswerCreateData> hospitalEvaluationAnswerList;

    @Getter
    @Builder
    public static class HospitalEvaluationAnswerCreateData {

        private Long hospitalEvaluationQuestionId;
        private Boolean answer;
    }
}
