package com.project.foradhd.domain.medicine.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.audit.BaseTimeEntity;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medicine_review")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MedicineReview extends BaseTimeEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "age_range", length = 50)
    private String ageRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @ElementCollection
    @CollectionTable(name = "medicine_co_medications", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "medicine_id")
    private List<Long> coMedications;

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