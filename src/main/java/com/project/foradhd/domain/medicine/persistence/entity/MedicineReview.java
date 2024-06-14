package com.project.foradhd.domain.medicine.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medicine_review")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineReview extends BaseTimeEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // UserPrivacy의 ageRange를 저장하기 위한 필드 추가
    @Column(name = "age_range", length = 50)
    private String ageRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "medicine_co_medications",
            joinColumns = @JoinColumn(name = "review_id"),  // MedicineReview 엔티티의 PK를 참조
            inverseJoinColumns = @JoinColumn(name = "id")   // Medicine 엔티티의 PK (id)를 참조
    )
    private List<Medicine> coMedications;


    @Column(length = 1500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    private Medicine medicine;

    @ElementCollection
    @CollectionTable(name = "medicine_review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Column(nullable = false)
    private float grade;

    @Column(nullable = false)
    private int helpCount;

}
