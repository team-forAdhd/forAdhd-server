package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HospitalEvaluationQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_evaluation_question_id")
    private Long id;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(nullable = false)
    private Integer seq;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;
}
