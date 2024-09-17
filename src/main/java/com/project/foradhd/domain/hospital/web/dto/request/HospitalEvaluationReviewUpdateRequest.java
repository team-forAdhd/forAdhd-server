package com.project.foradhd.domain.hospital.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalEvaluationReviewUpdateRequest {

    @NotEmpty(message = "{hospitalEvaluationAnswerList.notEmpty}")
    private List<@Valid HospitalEvaluationAnswerUpdateRequest> hospitalEvaluationAnswerList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HospitalEvaluationAnswerUpdateRequest {

        @NotNull(message = "{hospitalEvaluationQuestion.id.notNull}")
        private Long hospitalEvaluationQuestionId;
        @NotNull(message = "{hospitalEvaluationAnswer.answer.notNull}")
        private Boolean answer;
    }
}
