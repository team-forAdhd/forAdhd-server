package com.project.foradhd.domain.user.persistence.entity;

import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserPrivacy extends BaseTimeEntity {

    @Id
    @GenericGenerator(name = "uuid-generator", type = com.project.foradhd.global.util.UUIDGenerator.class)
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "user_privacy_id", columnDefinition = "varchar(32)")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column
    private LocalDate birth;

    @Column(length = 50)
    private String ageRange;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.UNKNOWN;

    @PostLoad
    private void calculateAgeRange() {
        if (this.birth != null) {
            int age = Period.between(this.birth, LocalDate.now()).getYears();
            if (age < 20) {
                this.ageRange = "10대";
            } else if (age < 30) {
                this.ageRange = "20대";
            } else if (age < 40) {
                this.ageRange = "30대";
            } else if (age < 50) {
                this.ageRange = "40대";
            } else {
                this.ageRange = "50대 이상";
            }
        } else {
            this.ageRange = "Unknown";
        }
    }
}
