package com.project.foradhd.domain.hospital.business.dto.out;

import com.project.foradhd.domain.hospital.persistence.entity.HospitalEvaluationQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HospitalEvaluationQuestionListData {

    private List<HospitalEvaluationQuestion> hospitalEvaluationQuestionList;
}
