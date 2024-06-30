package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HospitalEvaluationAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_evaluation_answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_evaluation_review_id", nullable = false)
    private HospitalEvaluationReview hospitalEvaluationReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_evaluation_question_id", nullable = false)
    private HospitalEvaluationQuestion hospitalEvaluationQuestion;

    @Column(nullable = false)
    private Boolean answer;

    public void updateHospitalEvaluationReview(HospitalEvaluationReview hospitalEvaluationReview) {
        this.hospitalEvaluationReview = hospitalEvaluationReview;
    }

    public void updateAnswer(Boolean answer) {
        this.answer = answer;
    }
}
