package com.project.foradhd.domain.user.persistence.entity;

import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.enums.Role;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GenericGenerator(name = "uuid-generator", type = com.project.foradhd.global.util.UUIDGenerator.class)
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "user_id")
    private String id;

    @Column(unique = true, length = 500)
    private String snsUserId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 500)
    private String password;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ANONYMOUS;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Provider provider = Provider.FOR_A;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column
    private LocalDate birth;

    @Column(length = 50)
    private String ageRange;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.UNKNOWN;

    private String profileImage;

    @Builder.Default
    @ColumnDefault(value = "0")
    @Column(nullable = false)
    private Boolean isVerifiedEmail = Boolean.FALSE;

    @Builder.Default
    @ColumnDefault(value = "0")
    @Column(nullable = false)
    private Boolean isAdhd = Boolean.FALSE;

    @Builder.Default
    @ColumnDefault(value = "0")
    @Column(nullable = false)
    private Boolean pushNotificationAgree = Boolean.FALSE;

    @Column(length = 50)
    private String phoneModel;

    private String deviceToken;

    public String getAuthority() {
        return this.role.getName();
    }

    public void updateProfile(String nickname, String profileImage, Boolean isAdhd) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.isAdhd = isAdhd;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updatePushNotificationAgree(Boolean pushNotificationAgree) {
        this.pushNotificationAgree = pushNotificationAgree;
    }

    public User loginBySns(User snsUser) {
        this.name = snsUser.name;
        this.email = snsUser.email;
        this.gender = snsUser.gender;
        this.ageRange = snsUser.ageRange;
        this.birth = snsUser.birth;
        return this;
    }

    public void snsSignUp(User user) {
        this.role = Role.USER;
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.isAdhd = user.getIsAdhd();
        this.pushNotificationAgree = user.getPushNotificationAgree();
    }
}
