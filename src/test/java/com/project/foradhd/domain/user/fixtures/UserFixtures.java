package com.project.foradhd.domain.user.fixtures;

import com.project.foradhd.domain.user.persistence.entity.PushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.PushNotificationApproval.PushNotificationApprovalBuilder;
import com.project.foradhd.domain.user.persistence.entity.Terms;
import com.project.foradhd.domain.user.persistence.entity.Terms.TermsBuilder;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.User.UserBuilder;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy.UserPrivacyBuilder;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.entity.UserProfile.UserProfileBuilder;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.PushNotificationType;
import com.project.foradhd.domain.user.persistence.enums.Role;
import java.time.LocalDate;

public class UserFixtures {

    public static UserBuilder toUser() {
        return User.builder()
            .id("userId")
            .email("jkde7721@naver.com")
            .role(Role.USER)
            .isVerifiedEmail(true);
    }

    public static UserPrivacyBuilder toUserPrivacy() {
        return UserPrivacy.builder()
            .id("userPrivacyId")
            .user(toUser().build())
            .name("김다은")
            .birth(LocalDate.of(1999, 2, 1))
            .ageRange("20-30")
            .gender(Gender.FEMALE);
    }

    public static UserProfileBuilder toUserProfile() {
        return UserProfile.builder()
            .id("userProfileId")
            .user(toUser().build())
            .nickname("단이")
            .profileImage("https://image.png")
            .isAdhd(false);
    }

    public static TermsBuilder toPrivacyTerms() {
        return Terms.builder()
            .id(1L)
            .seq(1L)
            .title("개인정보 약관")
            .content("개인정보 약관 내용")
            .required(true);
    }

    public static TermsBuilder toThirdPartyPrivacyTerms() {
        return Terms.builder()
            .id(2L)
            .seq(2L)
            .title("제3자 개인정보 제공 동의")
            .content("제3자 개인정보 제공 동의 내용")
            .required(false);
    }

    public static TermsBuilder toMarketingTerms() {
        return Terms.builder()
            .id(3L)
            .seq(3L)
            .title("마케팅 수신 동의")
            .content("마케팅 수신 동의 내용")
            .required(false);
    }

    public static PushNotificationApprovalBuilder toAllPushNotificationApproval() {
        return PushNotificationApproval.builder()
            .id(1L)
            .pushNotificationType(PushNotificationType.ALL);
    }
}
