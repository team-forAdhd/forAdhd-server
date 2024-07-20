package com.project.foradhd.domain.hospital.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class HospitalEvaluationReviewCreateRequest {

    @NotEmpty(message = "{hospitalEvaluationAnswerList.notEmpty}")
    private List<@Valid HospitalEvaluationAnswerCreateRequest> hospitalEvaluationAnswerList;

    @Getter
    public static class HospitalEvaluationAnswerCreateRequest {

        @NotNull(message = "{hospitalEvaluationQuestion.id.notNull}")
        private Long hospitalEvaluationQuestionId;
        @NotNull(message = "{hospitalEvaluationAnswer.answer.notNull}")
        private Boolean answer;
    }
}
