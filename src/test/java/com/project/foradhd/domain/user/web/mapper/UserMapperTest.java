package com.project.foradhd.domain.user.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.enums.Gender;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
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
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .name("김다은")
            .birth(LocalDate.of(1999, 2, 1))
            .gender(Gender.FEMALE)
            .email("jkde7721@naver.com")
            .password("abc123!")
            .passwordConfirm("abc123!")
            .nickname("단이")
            .profileImage("http://")
            .isAdhd(false)
            .pushNotificationAgree(true)
            .termsApprovals(List.of(termsApprovalRequest1, termsApprovalRequest2, termsApprovalRequest3))
            .build();

        //when
        SignUpData signUpData = userMapper.toSignUpData(signUpRequest);
        User user = signUpData.getUser();
        List<UserTermsApproval> userTermsApprovals = signUpData.getUserTermsApprovals();

        //then
        assertThat(user.getName()).isEqualTo(signUpRequest.getName());
        assertThat(user.getBirth()).isEqualTo(signUpRequest.getBirth());
        assertThat(user.getGender()).isEqualTo(signUpRequest.getGender());
        assertThat(user.getEmail()).isEqualTo(signUpRequest.getEmail());
        assertThat(user.getPassword()).isNull();
        assertThat(user.getNickname()).isEqualTo(signUpRequest.getNickname());
        assertThat(user.getProfileImage()).isEqualTo(signUpRequest.getProfileImage());
        assertThat(user.getIsAdhd()).isEqualTo(signUpRequest.getIsAdhd());
        assertThat(user.getPushNotificationAgree()).isEqualTo(signUpRequest.getPushNotificationAgree());

        assertThat(userTermsApprovals).extracting("id.user", "id.terms.id", "approved")
            .containsExactlyInAnyOrder(
                tuple(user, termsApprovalRequest1.getTermsId(), termsApprovalRequest1.getApproved()),
                tuple(user, termsApprovalRequest2.getTermsId(), termsApprovalRequest2.getApproved()),
                tuple(user, termsApprovalRequest3.getTermsId(), termsApprovalRequest3.getApproved()));
    }

    @TestConfiguration
    static class UserMapperTestConfig {

        @Bean
        public UserMapper userMapper() {
            return Mappers.getMapper(UserMapper.class);
        }
    }
}
