package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.audit.BaseTimeEntity;
import com.project.foradhd.global.util.UUIDGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HospitalEvaluationReview extends BaseTimeEntity {

    @Id
    @GenericGenerator(name = "uuid-generator", type = UUIDGenerator.class)
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "hospital_evaluation_review_id", columnDefinition = "varchar(32)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @OneToMany(mappedBy = "hospitalEvaluationReview",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<HospitalEvaluationAnswer> hospitalEvaluationAnswerList;

    //연관관계 매핑 편의 메소드
    public void updateHospitalEvaluationAnswerList(List<HospitalEvaluationAnswer> hospitalEvaluationAnswerList) {
        this.hospitalEvaluationAnswerList = hospitalEvaluationAnswerList;
        hospitalEvaluationAnswerList.forEach(hospitalEvaluationAnswer -> hospitalEvaluationAnswer.updateHospitalEvaluationReview(this));
    }
}
