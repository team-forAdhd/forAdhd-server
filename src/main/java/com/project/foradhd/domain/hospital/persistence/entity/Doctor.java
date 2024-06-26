package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Doctor extends BaseTimeEntity {

    @Id
    @GenericGenerator(name = "uuid-generator", type = com.project.foradhd.global.util.UUIDGenerator.class)
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "doctor_id", columnDefinition = "varchar(32)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false)
    private String name;

    private String image;

    @Column(columnDefinition = "longtext")
    private String profile;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer totalReceiptReviewCount = 0;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    //TODO: 아래 메소드 모두 수정
    public Double calculateTotalGrade() {
        return null;
    }

    public Long calculateTotalReviewCount() {
        return (long) totalReceiptReviewCount;
    }

    public void updateByCreatedReceiptReview(Integer receiptReviewTotalGradeSum) {
        totalReceiptReviewCount++;
    }

    public void updateByDeletedReceiptReview(Integer receiptReviewTotalGradeSum) {
        totalReceiptReviewCount--;
    }

    public void updateByCreatedBriefReview(Integer briefReviewTotalGradeSum) {
    }

    public void updateByDeletedBriefReview(Integer briefReviewTotalGradeSum) {
    }
}
