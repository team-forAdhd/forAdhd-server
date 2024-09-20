package com.project.foradhd.domain.user.business.service;

import static com.project.foradhd.domain.user.fixtures.UserFixtures.toMarketingTerms;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toPrivacyTerms;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toAllPushNotificationApproval;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toThirdPartyPrivacyTerms;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUser;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUserPrivacy;
import static com.project.foradhd.domain.user.fixtures.UserFixtures.toUserProfile;
import static com.project.foradhd.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.project.foradhd.domain.user.business.dto.in.PasswordUpdateData;
import com.project.foradhd.domain.user.business.dto.in.ProfileUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PushNotificationApprovalUpdateData;
import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.business.dto.in.SnsSignUpData;
import com.project.foradhd.domain.user.business.dto.in.TermsApprovalsUpdateData;
import com.project.foradhd.domain.user.business.service.impl.UserServiceImpl;
import com.project.foradhd.domain.user.persistence.entity.PushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.Terms;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval.UserPushNotificationApprovalId;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval.UserTermsApprovalId;
import com.project.foradhd.domain.user.persistence.enums.ForAdhdType;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.persistence.enums.Role;
import com.project.foradhd.domain.user.persistence.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.project.foradhd.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UserService 테스트")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserPrivacyRepository userPrivacyRepository;

    @Mock
    UserProfileRepository userProfileRepository;

    @Mock
    TermsRepository termsRepository;

    @Mock
    UserTermsApprovalRepository userTermsApprovalRepository;

    @Mock
    PushNotificationApprovalRepository pushNotificationApprovalRepository;

    @Mock
    UserPushNotificationApprovalRepository userPushNotificationApprovalRepository;

    @Mock
    UserDeviceRepository userDeviceRepository;

    @Mock
    UserAuthInfoService userAuthInfoService;

    @DisplayName("유저 닉네임 중복 여부 확인 로직 테스트")
    @Test
    void check_nickname_test() {
        //given
        String nickname = "ForA";
        given(userProfileRepository.findByNickname(nickname))
            .willReturn(Optional.empty());

        //when
        boolean isValidNickname = userService.checkNickname(nickname);

        //then
        assertThat(isValidNickname).isTrue();
    }

    @DisplayName("일반 회원가입 테스트 - 성공")
    @Test
    void sign_up_test() {
        //given
        String password = "userPassword";
        User newUser = toUser().build();
        UserPrivacy newUserPrivacy = toUserPrivacy().user(newUser).build();
        UserProfile newUserProfile = toUserProfile().user(newUser).build();
        Terms privacyTerms = toPrivacyTerms().build();
        Terms thirdPartyPrivacyTerms = toThirdPartyPrivacyTerms().build();
        Terms marketingTerms = toMarketingTerms().build();
        PushNotificationApproval allPushNotificationApproval = toAllPushNotificationApproval().build();
        List<UserTermsApproval> newUserTermsApprovals = List.of(
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(newUser, privacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(newUser, thirdPartyPrivacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(newUser, marketingTerms))
                .approved(true)
                .build());
        List<UserPushNotificationApproval> newUserPushNotificationApprovals = List.of(
            UserPushNotificationApproval.builder()
                .id(new UserPushNotificationApprovalId(newUser, allPushNotificationApproval))
                .approved(true)
                .build());
        SignUpData signUpData = SignUpData.builder()
            .user(newUser)
            .userPrivacy(newUserPrivacy)
            .userProfile(newUserProfile)
            .password(password)
            .userTermsApprovals(newUserTermsApprovals)
            .userPushNotificationApprovals(newUserPushNotificationApprovals)
            .build();

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(userProfileRepository.findByNickname(anyString())).willReturn(Optional.empty());
        given(termsRepository.findAll()).willReturn(List.of(privacyTerms, thirdPartyPrivacyTerms, marketingTerms));
        given(pushNotificationApprovalRepository.findAll()).willReturn(List.of(allPushNotificationApproval));

        //when
        User signedUpUser = userService.signUp(signUpData);

        //then
        assertThat(signedUpUser).isEqualTo(newUser);
        then(userRepository).should(times(1)).findByEmail(newUser.getEmail());
        then(userProfileRepository).should(times(1)).findByNickname(newUserProfile.getNickname());
        then(termsRepository).should(times(1)).findAll();
        then(pushNotificationApprovalRepository).should(times(1)).findAll();

        then(userRepository).should(times(1)).save(newUser);
        then(userPrivacyRepository).should(times(1)).save(newUserPrivacy);
        then(userProfileRepository).should(times(1)).save(newUserProfile);
        then(userTermsApprovalRepository).should(times(1)).saveAll(newUserTermsApprovals);
        then(userPushNotificationApprovalRepository).should(times(1)).saveAll(newUserPushNotificationApprovals);
        then(userAuthInfoService).should(times(1)).signUpByPassword(newUser, password);
    }

    @DisplayName("SNS 회원가입 테스트")
    @Test
    void sns_sign_up_test() {
        //given
        User newUser = toUser()
            .isVerifiedEmail(true)
            .role(Role.GUEST)
            .build();
        UserProfile newUserProfile = toUserProfile().user(newUser).build();
        Terms privacyTerms = toPrivacyTerms().build();
        Terms thirdPartyPrivacyTerms = toThirdPartyPrivacyTerms().build();
        Terms marketingTerms = toMarketingTerms().build();
        PushNotificationApproval allPushNotificationApproval = toAllPushNotificationApproval().build();
        List<UserTermsApproval> newUserTermsApprovals = List.of(
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(newUser, privacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(newUser, thirdPartyPrivacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(newUser, marketingTerms))
                .approved(true)
                .build());
        List<UserPushNotificationApproval> newUserPushNotificationApprovals = List.of(
            UserPushNotificationApproval.builder()
                .id(new UserPushNotificationApprovalId(newUser, allPushNotificationApproval))
                .approved(true)
                .build());
        SnsSignUpData snsSignUpData = SnsSignUpData.builder()
            .userProfile(newUserProfile)
            .userTermsApprovals(newUserTermsApprovals)
            .userPushNotificationApprovals(newUserPushNotificationApprovals)
            .build();

        given(userProfileRepository.findByNickname(anyString())).willReturn(Optional.empty());
        given(termsRepository.findAll()).willReturn(List.of(privacyTerms, thirdPartyPrivacyTerms, marketingTerms));
        given(pushNotificationApprovalRepository.findAll()).willReturn(List.of(allPushNotificationApproval));
        given(userRepository.findById(newUser.getId())).willReturn(Optional.of(newUser));
        assertThat(newUser.getRole()).isEqualTo(Role.GUEST);

        //when
        User snsSignedUpUser = userService.snsSignUp(newUser.getId(), snsSignUpData);

        //then
        assertThat(snsSignedUpUser.getRole()).isEqualTo(Role.USER);
        then(userProfileRepository).should(times(1)).findByNickname(newUserProfile.getNickname());
        then(termsRepository).should(times(1)).findAll();
        then(pushNotificationApprovalRepository).should(times(1)).findAll();
        then(userRepository).should(times(1)).findById(newUser.getId());

        then(userProfileRepository).should(times(1)).save(newUserProfile);
        then(userTermsApprovalRepository).should(times(1)).saveAll(newUserTermsApprovals);
        then(userPushNotificationApprovalRepository).should(times(1)).saveAll(newUserPushNotificationApprovals);
    }

    @DisplayName("프로필 수정 테스트")
    @Test
    void update_profile_test() {
        //given
        String userId = "userId";
        UserProfile originUserProfile = UserProfile.builder()
            .nickname("단이")
            .profileImage("https://image.png")
            .forAdhdType(ForAdhdType.FOR_AROUND_ADHD)
            .build();
        UserProfile newUserProfile = UserProfile.builder()
            .nickname("김다")
            .profileImage("https://image.jpg")
            .forAdhdType(ForAdhdType.FOR_MY_ADHD)
            .build();
        ProfileUpdateData profileUpdateData = ProfileUpdateData.builder()
            .userProfile(newUserProfile)
            .build();
        given(userProfileRepository.findByNicknameAndUserIdNot(newUserProfile.getNickname(), userId))
            .willReturn(Optional.empty());
        given(userProfileRepository.findByUserId(userId)).willReturn(Optional.of(originUserProfile));

        //when
        userService.updateProfile(userId, profileUpdateData);

        //then
        assertThat(originUserProfile.getNickname()).isEqualTo(newUserProfile.getNickname());
        assertThat(originUserProfile.getProfileImage()).isEqualTo(newUserProfile.getProfileImage());
        assertThat(originUserProfile.getForAdhdType()).isEqualTo(newUserProfile.getForAdhdType());
        then(userProfileRepository).should(times(1))
            .findByNicknameAndUserIdNot(newUserProfile.getNickname(), userId);
        then(userProfileRepository).should(times(1)).findByUserId(userId);
    }

    @DisplayName("프로필 수정 테스트 - 실패: 닉네임 중복")
    @Test
    void update_profile_test_fail_duplicated_nickname() {
        //given
        String userId = "userId";
        UserProfile anotherUser = UserProfile.builder()
            .nickname("김다")
            .profileImage("https://image.jpeg")
            .forAdhdType(ForAdhdType.FOR_MY_ADHD)
            .build();
        UserProfile newUserProfile = UserProfile.builder()
            .nickname("김다")
            .profileImage("https://image.jpg")
            .forAdhdType(ForAdhdType.FOR_MY_ADHD)
            .build();
        ProfileUpdateData profileUpdateData = ProfileUpdateData.builder()
            .userProfile(newUserProfile)
            .build();
        given(userProfileRepository.findByNicknameAndUserIdNot(newUserProfile.getNickname(), userId))
            .willReturn(Optional.of(anotherUser));

        //when, then
        assertThatThrownBy(() -> userService.updateProfile(userId, profileUpdateData))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ALREADY_EXISTS_NICKNAME);
        then(userProfileRepository).should(times(1))
            .findByNicknameAndUserIdNot(newUserProfile.getNickname(), userId);
        then(userProfileRepository).should(never()).findByUserId(userId);
    }

    @DisplayName("비밀번호 수정 테스트")
    @Test
    void update_password_test() {
        //given
        String userId = "userId";
        String prevPassword = "prevPassword";
        String newPassword = "newPassword";
        PasswordUpdateData passwordUpdateData = PasswordUpdateData.builder()
            .prevPassword(prevPassword)
            .password(newPassword)
            .passwordConfirm(newPassword)
            .build();

        //when
        userService.updatePassword(userId, passwordUpdateData);

        //then
        then(userAuthInfoService).should(times(1)).validatePasswordMatches(userId, prevPassword);
        then(userAuthInfoService).should(times(1)).updatePassword(userId, newPassword);
    }

    @DisplayName("이메일 인증 후 유저 인증 정보 수정 테스트")
    @Test
    void update_email_auth_test() {
        //given
        String prevEmail = "prevEmail@naver.com";
        String newEmail = "newEmail@naver.com";
        User user = toUser()
                .email(prevEmail)
                .isVerifiedEmail(false)
                .role(Role.GUEST)
                .build();
        given(userRepository.findById(anyString())).willReturn(Optional.of(user));
        given(userRepository.findByIdWithProfile(anyString())).willReturn(Optional.of(user));

        //when
        User updatedUser = userService.updateEmailAuth(user.getId(), newEmail);

        //then
        assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
        assertThat(updatedUser.getIsVerifiedEmail()).isTrue();
        assertThat(updatedUser.getRole()).isEqualTo(Role.USER);
        then(userRepository).should(times(1)).findById(user.getId());
        then(userRepository).should(times(1)).findByIdWithProfile(user.getId());
    }

    @DisplayName("이메일 인증 후 유저 인증 정보 수정 테스트 - 유저 프로필 미등록 상태인 경우 USER Role 획득 불가")
    @Test
    void update_email_auth_without_profile_test() {
        //given
        String prevEmail = "prevEmail@naver.com";
        String newEmail = "newEmail@naver.com";
        User user = toUser()
                .email(prevEmail)
                .isVerifiedEmail(false)
                .role(Role.GUEST)
                .build();
        given(userRepository.findById(anyString())).willReturn(Optional.of(user));
        given(userRepository.findByIdWithProfile(anyString())).willReturn(Optional.empty());

        //when
        User updatedUser = userService.updateEmailAuth(user.getId(), newEmail);

        //then
        assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
        assertThat(updatedUser.getIsVerifiedEmail()).isTrue();
        assertThat(updatedUser.getRole()).isEqualTo(Role.GUEST);
        then(userRepository).should(times(1)).findById(user.getId());
        then(userRepository).should(times(1)).findByIdWithProfile(user.getId());
    }

    @DisplayName("푸시 알림 동의 여부 수정 테스트")
    @Test
    void update_push_notification_approvals_test() {
        //given
        User user = toUser().build();
        PushNotificationApproval allPushNotificationApproval = toAllPushNotificationApproval().build();
        List<UserPushNotificationApproval> userPushNotificationApprovals = List.of(
            UserPushNotificationApproval.builder()
                .id(new UserPushNotificationApprovalId(user, allPushNotificationApproval))
                .approved(true)
                .build());
        PushNotificationApprovalUpdateData pushNotificationApprovalUpdateData =
            new PushNotificationApprovalUpdateData(userPushNotificationApprovals);
        given(pushNotificationApprovalRepository.findAll()).willReturn(List.of(allPushNotificationApproval));

        //when
        userService.updatePushNotificationApprovals(pushNotificationApprovalUpdateData);

        //then
        then(pushNotificationApprovalRepository).should(times(1)).findAll();
        then(userPushNotificationApprovalRepository).should(times(1)).saveAll(userPushNotificationApprovals);
    }

    @DisplayName("푸시 알림 동의 여부 수정 테스트 - 실패: 존재하지 않는 푸시 알림 동의 항목")
    @Test
    void update_push_notification_approvals_test_fail_not_existed_push_notification_approval() {
        //given
        User user = toUser().build();
        PushNotificationApproval allPushNotificationApproval = toAllPushNotificationApproval().build();
        List<UserPushNotificationApproval> userPushNotificationApprovals = List.of(
            UserPushNotificationApproval.builder()
                .id(new UserPushNotificationApprovalId(user, allPushNotificationApproval))
                .approved(true)
                .build());
        PushNotificationApprovalUpdateData pushNotificationApprovalUpdateData =
            new PushNotificationApprovalUpdateData(userPushNotificationApprovals);
        given(pushNotificationApprovalRepository.findAll()).willReturn(List.of());

        //when, then
        assertThatThrownBy(() -> userService.updatePushNotificationApprovals(pushNotificationApprovalUpdateData))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_PUSH_NOTIFICATION_APPROVAL);
        then(pushNotificationApprovalRepository).should(times(1)).findAll();
        then(userPushNotificationApprovalRepository).should(never()).saveAll(userPushNotificationApprovals);
    }

    @DisplayName("이용약관 동의 여부 수정 테스트")
    @Test
    void update_terms_approvals_test() {
        //given
        User user = toUser()
            .isVerifiedEmail(true)
            .role(Role.GUEST)
            .build();
        Terms privacyTerms = toPrivacyTerms().build();
        Terms thirdPartyPrivacyTerms = toThirdPartyPrivacyTerms().build();
        Terms marketingTerms = toMarketingTerms().build();
        List<UserTermsApproval> userTermsApprovals = List.of(
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(user, privacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(user, thirdPartyPrivacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(user, marketingTerms))
                .approved(true)
                .build());
        TermsApprovalsUpdateData termsApprovalsUpdateData =
            new TermsApprovalsUpdateData(userTermsApprovals);
        given(termsRepository.findAll()).willReturn(List.of(privacyTerms, thirdPartyPrivacyTerms, marketingTerms));

        //when
        userService.updateTermsApprovals(termsApprovalsUpdateData);

        //then
        then(termsRepository).should(times(1)).findAll();
        then(userTermsApprovalRepository).should(times(1)).saveAll(userTermsApprovals);
    }

    @DisplayName("이용약관 동의 여부 수정 테스트 - 실패: 존재하지 않는 이용약관")
    @Test
    void update_terms_approvals_test_fail_not_existed_terms() {
        //given
        User user = toUser()
            .isVerifiedEmail(true)
            .role(Role.GUEST)
            .build();
        Terms privacyTerms = toPrivacyTerms().build();
        Terms thirdPartyPrivacyTerms = toThirdPartyPrivacyTerms().build();
        Terms marketingTerms = toMarketingTerms().build();
        List<UserTermsApproval> userTermsApprovals = List.of(
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(user, privacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(user, thirdPartyPrivacyTerms))
                .approved(true)
                .build(),
            UserTermsApproval.builder()
                .id(new UserTermsApprovalId(user, marketingTerms))
                .approved(true)
                .build());
        TermsApprovalsUpdateData termsApprovalsUpdateData =
            new TermsApprovalsUpdateData(userTermsApprovals);
        given(termsRepository.findAll()).willReturn(List.of(privacyTerms, thirdPartyPrivacyTerms));

        //when, then
        assertThatThrownBy(() -> userService.updateTermsApprovals(termsApprovalsUpdateData))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_TERMS);
        then(termsRepository).should(times(1)).findAll();
        then(userTermsApprovalRepository).should(never()).saveAll(userTermsApprovals);
    }

    @DisplayName("유저 탈퇴 로직 테스트")
    @Test
    void withdraw_test() {
        //given
        String userId = "userId";
        User withdrawnUser = toUser().build();
        UserProfile withdrawnUserProfile = toUserProfile().build();
        UserPrivacy withdrawnUserPrivacy = toUserPrivacy().build();
        given(userRepository.findById(userId)).willReturn(Optional.of(withdrawnUser));
        given(userProfileRepository.findByUserId(userId)).willReturn(Optional.of(withdrawnUserProfile));
        given(userPrivacyRepository.findByUserId(userId)).willReturn(Optional.of(withdrawnUserPrivacy));

        //when
        userService.withdraw(userId);

        //then
        then(userTermsApprovalRepository).should(times(1)).deleteByUserId(userId);
        then(userPushNotificationApprovalRepository).should(times(1)).deleteByUserId(userId);
        then(userDeviceRepository).should(times(1)).deleteByUserId(userId);
        then(userAuthInfoService).should(times(1)).withdraw(userId);

        assertThat(withdrawnUser.getEmail()).isEmpty();
        assertThat(withdrawnUser.getRole()).isEqualTo(Role.ANONYMOUS);
        assertThat(withdrawnUser.getIsVerifiedEmail()).isFalse();
        assertThat(withdrawnUser.getDeleted()).isTrue();
        assertThat(withdrawnUser.getDeletedAt()).isNotNull();

        assertThat(withdrawnUserProfile.getNickname()).isEmpty();
        assertThat(withdrawnUserProfile.getProfileImage()).isEmpty();

        assertThat(withdrawnUserPrivacy.getName()).isEmpty();
        assertThat(withdrawnUserPrivacy.getBirth()).isEqualTo(LocalDate.of(1970, 1, 1));
        assertThat(withdrawnUserPrivacy.getAgeRange()).isEmpty();
        assertThat(withdrawnUserPrivacy.getGender()).isEqualTo(Gender.UNKNOWN);
    }

    @DisplayName("ID로 User 조회 테스트 - 실패")
    @Test
    void get_user_test_fail() {
        //given
        String userId = "userId";
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userService.getUser(userId))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_USER);
    }

    @DisplayName("ID로 UserProfile 조회 테스트 - 실패")
    @Test
    void get_user_profile_test_fail() {
        //given
        String userId = "userId";
        given(userProfileRepository.findByUserId(userId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userService.getUserProfile(userId))
            .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_USER_PROFILE);
    }

    @DisplayName("ID로 UserPrivacy 조회 테스트 - 실패")
    @Test
    void get_user_privacy_test_fail() {
        //given
        String userId = "userId";
        given(userPrivacyRepository.findByUserId(userId)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userService.getUserPrivacy(userId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_USER_PRIVACY);
    }

    @DisplayName("ID로 조회된 User의 이메일 인증 여부 테스트")
    @Test
    void has_verified_email_test() {
        //given
        String userId = "userId";
        User user = toUser()
            .id(userId)
            .isVerifiedEmail(true)
            .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        //when
        Boolean hasVerifiedEmail = userService.hasVerifiedEmail(userId);

        //then
        assertThat(hasVerifiedEmail).isTrue();
    }

    @DisplayName("ID로 조회된 User의 프로필 존재 여부 테스트")
    @Test
    void has_user_profile_test() {
        //given
        String userId = "userId";
        User user = toUser()
            .id(userId)
            .build();
        given(userRepository.findByIdWithProfile(userId)).willReturn(Optional.of(user));

        //when
        Boolean hasProfile = userService.hasUserProfile(userId);

        //then
        assertThat(hasProfile).isTrue();
    }
}
