package com.project.foradhd.domain.user.web.controller;

import com.project.foradhd.config.SecurityControllerTestConfig;
import com.project.foradhd.config.user.WithMockTestUser;
import com.project.foradhd.domain.user.business.dto.out.UserProfileDetailsData;
import com.project.foradhd.domain.user.business.dto.out.UserTokenData;
import com.project.foradhd.domain.user.business.service.UserEmailAuthService;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.business.service.UserTokenService;
import com.project.foradhd.domain.user.fixtures.UserFixtures;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.enums.ForAdhdType;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.web.dto.request.*;
import com.project.foradhd.domain.user.web.mapper.UserMapper;
import com.project.foradhd.global.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockTestUser
@ContextConfiguration(classes = SecurityControllerTestConfig.class)
@WebMvcTest(value = { UserController.class, UserMapper.class })
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserTokenService userTokenService;

    @MockBean
    UserEmailAuthService userEmailAuthService;

    @DisplayName("유저 닉네임 중복 체크 컨트롤러 테스트")
    @Test
    void check_nickname_test() throws Exception {
        //given
        String nickname = "단이";
        NicknameCheckRequest request = new NicknameCheckRequest(nickname);
        given(userService.checkNickname(anyString())).willReturn(true);

        //when, then
        mockMvc.perform(get("/api/v1/user/nickname-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValidNickname")
                        .value(true))
                .andDo(print());
        then(userService).should(times(1)).checkNickname(nickname);
    }

    @DisplayName("유저 닉네임 중복 체크 컨트롤러 테스트 - 실패: 빈 문자열 닉네임 요청")
    @Test
    void check_nickname_test_fail_with_blank_nickname() throws Exception {
        //given
        String nickname = "";
        NicknameCheckRequest request = new NicknameCheckRequest(nickname);
        given(userService.checkNickname(anyString())).willReturn(true);

        //when, then
        mockMvc.perform(get("/api/v1/user/nickname-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        then(userService).should(never()).checkNickname(nickname);
    }

    @DisplayName("유저 프로필 조회 컨트롤러 테스트")
    @Test
    void get_user_profile_details() throws Exception {
        //given
        UserProfile userProfile = UserFixtures.toUserProfile().build();
        UserProfileDetailsData userProfileDetailsData = new UserProfileDetailsData(userProfile);
        given(userService.getUserProfileDetails(anyString())).willReturn(userProfileDetailsData);

        //when, then
        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userProfile.getUser().getEmail()))
                .andExpect(jsonPath("$.nickname").value(userProfile.getNickname()))
                .andExpect(jsonPath("$.profileImage").value(userProfile.getProfileImage()))
                .andExpect(jsonPath("$.forAdhdType").value(userProfile.getForAdhdType().name()))
                .andDo(print());
        then(userService).should(times(1)).getUserProfileDetails(anyString());
    }

    @DisplayName("유저 일반 회원가입 컨트롤러 테스트")
    @Test
    void sign_up_test() throws Exception {
        //given
        List<SignUpRequest.TermsApprovalRequest> termsApprovals = List.of(SignUpRequest.TermsApprovalRequest.builder()
                        .termsId(1L)
                        .approved(true)
                        .build(),
                SignUpRequest.TermsApprovalRequest.builder()
                        .termsId(2L)
                        .approved(false)
                        .build());
        List<SignUpRequest.PushNotificationApprovalRequest> pushNotificationApprovals = List.of(SignUpRequest.PushNotificationApprovalRequest.builder()
                .pushNotificationApprovalId(1L)
                .approved(true)
                .build());
        String password = "testPassword123!";
        SignUpRequest request = SignUpRequest.builder()
                .name("김다은")
                .birth(LocalDate.of(1999, 2, 1))
                .gender(Gender.FEMALE)
                .email("jkde7721@naver.com")
                .password(PasswordRequest.builder()
                        .password(password)
                        .passwordConfirm(password)
                        .build())
                .nickname("김다")
                .profileImage("v2/images/profile.png")
                .forAdhdType(ForAdhdType.FOR_AROUND_ADHD)
                .termsApprovals(termsApprovals)
                .pushNotificationApprovals(pushNotificationApprovals)
                .build();
        User user = UserFixtures.toUser().build();
        UserTokenData userTokenData = new UserTokenData("accessToken", "refreshToken");
        given(userService.signUp(any())).willReturn(user);
        given(userTokenService.generateToken(any())).willReturn(userTokenData);

        //when, then
        mockMvc.perform(post("/api/v1/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value(userTokenData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(userTokenData.getRefreshToken()))
                .andExpect(jsonPath("$.hasVerifiedEmail").value(user.getIsVerifiedEmail()))
                .andDo(print());
        then(userService).should(times(1)).signUp(any());
        then(userTokenService).should(times(1)).generateToken(user);
    }

    @DisplayName("유저 일반 회원가입 컨트롤러 테스트 - 실패: 유효하지 않은 비밀번호(숫자, 영문, 특수문자 조합 8자리 이상(공백 문자 포함X))")
    @Test
    void sign_up_test_fail_invalid_password() throws Exception {
        //given
        List<SignUpRequest.TermsApprovalRequest> termsApprovals = List.of(SignUpRequest.TermsApprovalRequest.builder()
                        .termsId(1L)
                        .approved(true)
                        .build(),
                SignUpRequest.TermsApprovalRequest.builder()
                        .termsId(2L)
                        .approved(false)
                        .build());
        List<SignUpRequest.PushNotificationApprovalRequest> pushNotificationApprovals = List.of(SignUpRequest.PushNotificationApprovalRequest.builder()
                .pushNotificationApprovalId(1L)
                .approved(true)
                .build());
        String password = "shortPwd";
        SignUpRequest request = SignUpRequest.builder()
                .name("김다은")
                .birth(LocalDate.of(1999, 2, 1))
                .gender(Gender.FEMALE)
                .email("jkde7721@naver.com")
                .password(PasswordRequest.builder()
                        .password(password)
                        .passwordConfirm(password)
                        .build())
                .nickname("김다")
                .profileImage("v2/images/profile.png")
                .forAdhdType(ForAdhdType.FOR_AROUND_ADHD)
                .termsApprovals(termsApprovals)
                .pushNotificationApprovals(pushNotificationApprovals)
                .build();

        //when, then
        mockMvc.perform(post("/api/v1/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        then(userService).should(never()).signUp(any());
        then(userTokenService).should(never()).generateToken(any());
    }

    @DisplayName("유저 일반 회원가입 컨트롤러 테스트 - 실패: 비밀번호 확인 불일치")
    @Test
    void sign_up_test_fail_not_equals_password_confirm() throws Exception {
        //given
        List<SignUpRequest.TermsApprovalRequest> termsApprovals = List.of(SignUpRequest.TermsApprovalRequest.builder()
                        .termsId(1L)
                        .approved(true)
                        .build(),
                SignUpRequest.TermsApprovalRequest.builder()
                        .termsId(2L)
                        .approved(false)
                        .build());
        List<SignUpRequest.PushNotificationApprovalRequest> pushNotificationApprovals = List.of(SignUpRequest.PushNotificationApprovalRequest.builder()
                .pushNotificationApprovalId(1L)
                .approved(true)
                .build());
        String password = "testPassword123!";
        SignUpRequest request = SignUpRequest.builder()
                .name("김다은")
                .birth(LocalDate.of(1999, 2, 1))
                .gender(Gender.FEMALE)
                .email("jkde7721@naver.com")
                .password(PasswordRequest.builder()
                        .password(password)
                        .passwordConfirm(password.toUpperCase())
                        .build())
                .nickname("김다")
                .profileImage("v2/images/profile.png")
                .forAdhdType(ForAdhdType.FOR_AROUND_ADHD)
                .termsApprovals(termsApprovals)
                .pushNotificationApprovals(pushNotificationApprovals)
                .build();

        //when, then
        mockMvc.perform(post("/api/v1/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        then(userService).should(never()).signUp(any());
        then(userTokenService).should(never()).generateToken(any());
    }

    @DisplayName("유저 SNS 회원가입 컨트롤러 테스트")
    @Test
    void sns_sign_up_test() throws Exception {
        //given
        List<SnsSignUpRequest.TermsApprovalRequest> termsApprovals = List.of(SnsSignUpRequest.TermsApprovalRequest.builder()
                        .termsId(1L)
                        .approved(true)
                        .build(),
                SnsSignUpRequest.TermsApprovalRequest.builder()
                        .termsId(2L)
                        .approved(false)
                        .build());
        List<SnsSignUpRequest.PushNotificationApprovalRequest> pushNotificationApprovals = List.of(SnsSignUpRequest.PushNotificationApprovalRequest.builder()
                .pushNotificationApprovalId(1L)
                .approved(true)
                .build());
        SnsSignUpRequest request = SnsSignUpRequest.builder()
                .nickname("김다")
                .profileImage("v2/images/profile.png")
                .forAdhdType(ForAdhdType.FOR_AROUND_ADHD)
                .termsApprovals(termsApprovals)
                .pushNotificationApprovals(pushNotificationApprovals)
                .build();
        User user = UserFixtures.toUser().build();
        UserTokenData userTokenData = new UserTokenData("accessToken", "refreshToken");
        given(userService.snsSignUp(anyString(), any())).willReturn(user);
        given(userTokenService.generateToken(any())).willReturn(userTokenData);

        //when, then
        mockMvc.perform(post("/api/v1/user/sns-sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value(userTokenData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(userTokenData.getRefreshToken()))
                .andExpect(jsonPath("$.hasVerifiedEmail").value(user.getIsVerifiedEmail()))
                .andExpect(jsonPath("$.hasProfile").value(true))
                .andDo(print());
        then(userService).should(times(1)).snsSignUp(anyString(), any());
        then(userTokenService).should(times(1)).generateToken(user);
    }

    @DisplayName("유저 이메일 인증용 메일 전송 컨트롤러 테스트")
    @Test
    void authenticate_email_test() throws Exception {
        //given
        String email = "jkde7721@naver.com";
        EmailAuthRequest request = new EmailAuthRequest(email);
        willDoNothing().given(userEmailAuthService).authenticateEmail(anyString(), any());

        //when, then
        mockMvc.perform(post("/api/v1/user/email-auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
        then(userEmailAuthService).should(times(1)).authenticateEmail(anyString(), any());
    }

    @DisplayName("유저 이메일 인증용 메일 전송 컨트롤러 테스트 - 실패: 유효하지 않은 이메일")
    @Test
    void authenticate_email_test_fail_invalid_email() throws Exception {
        //given
        String email = "jkde7721$naver,com";
        EmailAuthRequest request = new EmailAuthRequest(email);

        //when, then
        mockMvc.perform(post("/api/v1/user/email-auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        then(userEmailAuthService).should(never()).authenticateEmail(anyString(), any());
    }

    @DisplayName("유저 이메일 인증 위한 인증 코드 검증 컨트롤러 테스트")
    @Test
    void validate_email_auth_test() throws Exception {
        //given
        String email = "jkde7721@naver.com";
        String authCode = "123456";
        EmailAuthValidationRequest request = new EmailAuthValidationRequest(email, authCode);
        User user = UserFixtures.toUser().build();
        UserTokenData userTokenData = new UserTokenData("accessToken", "refreshToken");
        given(userEmailAuthService.validateEmailAuth(anyString(), any())).willReturn(user);
        given(userTokenService.generateToken(any())).willReturn(userTokenData);

        //when, then
        mockMvc.perform(put("/api/v1/user/email-auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(userTokenData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(userTokenData.getRefreshToken()))
                .andDo(print());
        then(userEmailAuthService).should(times(1)).validateEmailAuth(anyString(), any());
        then(userTokenService).should(times(1)).generateToken(user);
    }

    @DisplayName("유저 이메일 인증 위한 인증 코드 검증 컨트롤러 테스트 - 실패: 유효하지 않은 인증 코드(6자리 숫자)")
    @Test
    void validate_email_auth_test_fail_invalid_auth_code() throws Exception {
        //given
        String email = "jkde7721@naver.com";
        String authCode = "1234";
        EmailAuthValidationRequest request = new EmailAuthValidationRequest(email, authCode);

        //when, then
        mockMvc.perform(put("/api/v1/user/email-auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        then(userEmailAuthService).should(never()).validateEmailAuth(anyString(), any());
        then(userTokenService).should(never()).generateToken(any());
    }

    @DisplayName("유저 프로필 수정 컨트롤러 테스트")
    @Test
    void update_profile_test() throws Exception {
        //given
        ProfileUpdateRequest request = ProfileUpdateRequest.builder()
                .nickname("김다")
                .profileImage("v2/image/profile.png")
                .forAdhdType(ForAdhdType.FOR_AROUND_ADHD)
                .build();
        willDoNothing().given(userService).updateProfile(anyString(), any());

        //when, then
        mockMvc.perform(put("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
        then(userService).should(times(1)).updateProfile(anyString(), any());
    }

    @DisplayName("유저 비밀번호 수정 컨트롤러 테스트")
    @Test
    void update_password_test() throws Exception {
        //given
        String prevPassword = "prevTestPassword123!";
        String newPassword = "newTestPassword123!";
        PasswordUpdateRequest request = PasswordUpdateRequest.builder()
                .prevPassword(prevPassword)
                .password(PasswordRequest.builder()
                        .password(newPassword)
                        .passwordConfirm(newPassword)
                        .build())
                .build();
        willDoNothing().given(userService).updatePassword(anyString(), any());

        //when, then
        mockMvc.perform(put("/api/v1/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
        then(userService).should(times(1)).updatePassword(anyString(), any());
    }

    @DisplayName("유저 푸시 알림 동의 여부 수정 컨트롤러 테스트")
    @Test
    void update_push_notification_approvals_test() throws Exception {
        //given
        List<PushNotificationApprovalUpdateRequest.PushNotificationApprovalRequest> pushNotificationApprovals = List.of(
                PushNotificationApprovalUpdateRequest.PushNotificationApprovalRequest.builder()
                        .pushNotificationApprovalId(1L)
                        .approved(true)
                        .build(),
                PushNotificationApprovalUpdateRequest.PushNotificationApprovalRequest.builder()
                        .pushNotificationApprovalId(2L)
                        .approved(false)
                        .build());
        PushNotificationApprovalUpdateRequest request = new PushNotificationApprovalUpdateRequest(pushNotificationApprovals);
        willDoNothing().given(userService).updatePushNotificationApprovals(any());

        //when, then
        mockMvc.perform(put("/api/v1/user/push-notification-approvals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
        then(userService).should(times(1)).updatePushNotificationApprovals(any());
    }

    @DisplayName("유저 이용약관 동의 여부 수정 컨트롤러 테스트")
    @Test
    void update_terms_approvals_test() throws Exception {
        //given
        List<TermsApprovalsUpdateRequest.TermsApprovalRequest> termsApprovals = List.of(
                TermsApprovalsUpdateRequest.TermsApprovalRequest.builder()
                        .termsId(1L)
                        .approved(true)
                        .build(),
                TermsApprovalsUpdateRequest.TermsApprovalRequest.builder()
                        .termsId(2L)
                        .approved(false)
                        .build());
        TermsApprovalsUpdateRequest request = new TermsApprovalsUpdateRequest(termsApprovals);
        willDoNothing().given(userService).updateTermsApprovals(any());

        //when, then
        mockMvc.perform(put("/api/v1/user/terms-approvals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
        then(userService).should(times(1)).updateTermsApprovals(any());
    }

    @DisplayName("유저 탈퇴 컨트롤러 테스트")
    @Test
    void withdraw_test() throws Exception {
        //given
        willDoNothing().given(userService).withdraw(anyString());

        //when, then
        mockMvc.perform(delete("/api/v1/user/withdraw"))
                .andExpect(status().isOk())
                .andDo(print());
        then(userService).should(times(1)).withdraw(anyString());
    }
}
