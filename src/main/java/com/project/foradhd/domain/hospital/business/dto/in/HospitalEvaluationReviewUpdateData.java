package com.project.foradhd.domain.hospital.business.dto.in;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalEvaluationReviewUpdateData {

    private List<HospitalEvaluationAnswerUpdateData> hospitalEvaluationAnswerList;

    @Getter
    @Builder
    public static class HospitalEvaluationAnswerUpdateData {

        private Long hospitalEvaluationQuestionId;
        private Boolean answer;
    }
}
