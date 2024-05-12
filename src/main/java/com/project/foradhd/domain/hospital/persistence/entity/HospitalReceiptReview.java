package com.project.foradhd.domain.hospital.persistence.entity;

import com.project.foradhd.domain.hospital.persistence.converter.StringListConverter;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HospitalReceiptReview extends BaseTimeEntity {

    @Id
    @GenericGenerator(name = "uuid-generator", type = com.project.foradhd.global.util.UUIDGenerator.class)
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "hospital_receipt_review_id", columnDefinition = "varchar(32)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private Integer kindness;

    @Column(nullable = false)
    private Integer adhdUnderstanding;

    @Column(nullable = false)
    private Integer enoughMedicalTime;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @Builder.Default
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "varchar(1000)")
    private List<String> images = List.of();

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer helpCount = 0;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    public Integer calculateTotalGradeSum() {
        return kindness + adhdUnderstanding + enoughMedicalTime;
    }
}
