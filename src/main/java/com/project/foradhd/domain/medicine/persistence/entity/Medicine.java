package com.project.foradhd.domain.medicine.persistence.entity;

import com.project.foradhd.domain.medicine.persistence.enums.Color;
import com.project.foradhd.domain.medicine.persistence.enums.DosageForm;
import com.project.foradhd.domain.medicine.persistence.enums.Shape;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "medicine")
public class Medicine extends BaseTimeEntity {

    @jakarta.persistence.Id
    @Id
    private String id;

    private String name;

    private String engname;

    private String manufacturer;

    private String images;

    private String keywords;

    private Float totalGrade;

    private Integer reviewCount;

    @Enumerated(EnumType.STRING)
    private DosageForm dosageForm;

    @Enumerated(EnumType.STRING)
    private Shape shape;

    @Enumerated(EnumType.STRING)
    private Color color;

    // 연관 관계 매핑
    @OneToMany(mappedBy = "medicine")
    private List<MedicineDetails> medicineDetails;

    @OneToMany(mappedBy = "medicine")
    private List<MedicineReview> medicineReviews;

    @OneToMany(mappedBy = "medicine")
    private List<MedicineBookmark> medicineBookmarks;
}
