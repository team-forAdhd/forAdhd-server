package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import static com.project.foradhd.global.util.AverageCalculator.calculateAverage;

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

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Long totalGradeSum = 0L;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer totalReceiptReviewCount = 0;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer totalBriefReviewCount = 0;

    private String image;

    @Column(columnDefinition = "longtext")
    private String profile;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    public Double calculateTotalGrade() {
        return calculateAverage(totalGradeSum, calculateTotalReviewCount() * 3);
    }

    public Long calculateTotalReviewCount() {
        return (long) totalReceiptReviewCount + totalBriefReviewCount;
    }

    public void updateByCreatedReceiptReview(Integer receiptReviewTotalGradeSum) {
        totalGradeSum += receiptReviewTotalGradeSum;
        totalReceiptReviewCount++;
    }

    public void updateByDeletedReceiptReview(Integer receiptReviewTotalGradeSum) {
        totalGradeSum -= receiptReviewTotalGradeSum;
        totalReceiptReviewCount--;
    }

    public void updateByCreatedBriefReview(Integer briefReviewTotalGradeSum) {
        totalGradeSum += briefReviewTotalGradeSum;
        totalBriefReviewCount++;
    }

    public void updateByDeletedBriefReview(Integer briefReviewTotalGradeSum) {
        totalGradeSum -= briefReviewTotalGradeSum;
        totalBriefReviewCount--;
    }
}
