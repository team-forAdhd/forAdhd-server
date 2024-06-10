package com.project.foradhd.domain.user.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.enums.ForAdhdType;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.web.dto.request.PasswordRequest;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest.PushNotificationApprovalRequest;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest.TermsApprovalRequest;
import com.project.foradhd.domain.user.web.mapper.UserMapperTest.UserMapperTestConfig;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("UserMapper 테스트")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserMapperTestConfig.class)
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @DisplayName("일반 회원가입 요청 -> 서비스 계층 DTO 변환 테스트: 복잡성으로 MapStruct의 자동 변환 사용X(default 메소드로 직접 구현)")
    @Test
    void mapSignUpRequestToSignUpDataTest() {
        //given
        TermsApprovalRequest termsApprovalRequest1 = TermsApprovalRequest.builder()
            .termsId(1L).approved(true).build();
        TermsApprovalRequest termsApprovalRequest2 = TermsApprovalRequest.builder()
            .termsId(2L).approved(true).build();
        TermsApprovalRequest termsApprovalRequest3 = TermsApprovalRequest.builder()
            .termsId(3L).approved(false).build();
        PushNotificationApprovalRequest pushNotificationApprovalRequest1 = PushNotificationApprovalRequest.builder()
            .pushNotificationApprovalId(1L).approved(true).build();
        PushNotificationApprovalRequest pushNotificationApprovalRequest2 = PushNotificationApprovalRequest.builder()
            .pushNotificationApprovalId(1L).approved(false).build();
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .name("김다은")
            .birth(LocalDate.of(1999, 2, 1))
            .gender(Gender.FEMALE)
            .email("jkde7721@naver.com")
            .password(new PasswordRequest("abc123!", "abc123!"))
            .nickname("단이")
            .profileImage("http://")
            .forAdhdType(ForAdhdType.FOR_MY_ADHD)
            .termsApprovals(List.of(termsApprovalRequest1, termsApprovalRequest2, termsApprovalRequest3))
            .pushNotificationApprovals(List.of(pushNotificationApprovalRequest1, pushNotificationApprovalRequest2))
            .build();

        //when
        SignUpData signUpData = userMapper.toSignUpData(signUpRequest);
        User user = signUpData.getUser();
        UserPrivacy userPrivacy = signUpData.getUserPrivacy();
        UserProfile userProfile = signUpData.getUserProfile();
        String password = signUpData.getPassword();
        List<UserTermsApproval> userTermsApprovals = signUpData.getUserTermsApprovals();
        List<UserPushNotificationApproval> userPushNotificationApprovals = signUpData.getUserPushNotificationApprovals();

        //then
        assertThat(user.getEmail()).isEqualTo(signUpRequest.getEmail());
        assertThat(userPrivacy.getName()).isEqualTo(signUpRequest.getName());
        assertThat(userPrivacy.getBirth()).isEqualTo(signUpRequest.getBirth());
        assertThat(userPrivacy.getGender()).isEqualTo(signUpRequest.getGender());
        assertThat(password).isEqualTo(signUpRequest.getPassword().getPassword());
        assertThat(userProfile.getNickname()).isEqualTo(signUpRequest.getNickname());
        assertThat(userProfile.getProfileImage()).isEqualTo(signUpRequest.getProfileImage());
        assertThat(userProfile.getForAdhdType()).isEqualTo(signUpRequest.getForAdhdType());
        assertThat(userTermsApprovals).extracting("id.user", "id.terms.id", "approved")
            .containsExactlyInAnyOrder(
                tuple(user, termsApprovalRequest1.getTermsId(), termsApprovalRequest1.getApproved()),
                tuple(user, termsApprovalRequest2.getTermsId(), termsApprovalRequest2.getApproved()),
                tuple(user, termsApprovalRequest3.getTermsId(), termsApprovalRequest3.getApproved()));
        assertThat(userPushNotificationApprovals).extracting("id.user", "id.pushNotificationApproval.id", "approved")
            .containsExactlyInAnyOrder(
                tuple(user, pushNotificationApprovalRequest1.getPushNotificationApprovalId(),
                    pushNotificationApprovalRequest1.getApproved()),
                tuple(user, pushNotificationApprovalRequest2.getPushNotificationApprovalId(),
                    pushNotificationApprovalRequest2.getApproved()));
    }

    @TestConfiguration
    static class UserMapperTestConfig {

        @Bean
        public UserMapper userMapper() {
            return Mappers.getMapper(UserMapper.class);
        }
    }
}
