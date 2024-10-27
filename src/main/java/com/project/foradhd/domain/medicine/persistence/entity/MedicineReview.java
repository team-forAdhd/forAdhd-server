package com.project.foradhd.domain.medicine.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medicine_review")
public class MedicineReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String ageRange;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.UNKNOWN;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @ElementCollection
    @CollectionTable(name = "medicine_co_medications", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "medicine_id")
    private List<Long> coMedications;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @ElementCollection
    @CollectionTable(name = "medicine_review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Column(nullable = false)
    private Double grade;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer helpCount = 0;
}
