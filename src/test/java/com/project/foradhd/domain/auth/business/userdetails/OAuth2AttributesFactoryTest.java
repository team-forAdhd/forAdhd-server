package com.project.foradhd.domain.auth.business.userdetails;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OAuth2AttributesFactory 테스트")
class OAuth2AttributesFactoryTest {

    @DisplayName("소셜 로그인 정보 팩토리 테스트 - 실패: 유효하지 않은 SNS")
    @Test
    void social_login_info_factory_test_fail_invalid_sns() {
        //given
        String registrationId = "invalidRegistrationId";
        String nameAttributesKey = "nameAttributesKey";
        Map<String, Object> attributes = Map.of();

        //when, then
        assertThatThrownBy(() -> OAuth2AttributesFactory.valueOf(registrationId, nameAttributesKey, attributes))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("유효하지 않은 SNS 입니다.");
    }
}
