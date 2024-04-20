package com.project.foradhd.global.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("HeaderUtil 테스트")
@Import(HeaderUtil.class)
@ExtendWith(SpringExtension.class)
class HeaderUtilTest {

    @Autowired
    HeaderUtil headerUtil;

    @DisplayName("HttpServletRequest 요청의 Authorization 헤더에서 토큰 파싱 테스트 - 성공")
    @Test
    void parse_token_from_authorization_header_success() {
        //given
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(AUTHORIZATION, "Bearer token");

        //when
        Optional<String> tokenOptional = headerUtil.parseToken(httpServletRequest);

        //then
        assertThat(tokenOptional).isPresent()
            .contains("token");
    }

    @DisplayName("HttpServletRequest 요청의 Authorization 헤더에서 토큰 파싱 테스트 - 실패: Authorization 헤더 없음")
    @Test
    void parse_token_from_authorization_header_fail_no_header() {
        //given
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        //when
        Optional<String> tokenOptional = headerUtil.parseToken(httpServletRequest);

        //then
        assertThat(tokenOptional).isEmpty();
    }

    @DisplayName("HttpServletRequest 요청의 Authorization 헤더에서 토큰 파싱 테스트 - 실패: Authorization 헤더 값 유효하지 않음")
    @Test
    void parse_token_from_authorization_header_fail_invalid_header() {
        //given
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(AUTHORIZATION, "Basic token");

        //when
        Optional<String> tokenOptional = headerUtil.parseToken(httpServletRequest);

        //then
        assertThat(tokenOptional).isEmpty();
    }
}
