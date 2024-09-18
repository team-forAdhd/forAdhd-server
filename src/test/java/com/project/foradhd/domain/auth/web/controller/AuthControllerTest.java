package com.project.foradhd.domain.auth.web.controller;

import com.project.foradhd.config.SecurityControllerTestConfig;
import com.project.foradhd.config.user.WithMockTestUser;
import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;
import com.project.foradhd.domain.auth.business.service.AuthService;
import com.project.foradhd.domain.auth.web.dto.request.TokenReissueRequest;
import com.project.foradhd.domain.auth.web.mapper.AuthMapper;
import com.project.foradhd.global.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockTestUser
@ContextConfiguration(classes = SecurityControllerTestConfig.class)
@WebMvcTest(value = { AuthController.class, AuthMapper.class })
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @DisplayName("인증 토큰 재발급 컨트롤러 테스트")
    @Test
    void reissue_test() throws Exception {
        //given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenReissueRequest request = new TokenReissueRequest(accessToken, refreshToken);

        String reissuedAccessToken = "reissuedAccessToken";
        String reissuedRefreshToken = "reissuedRefreshToken";
        AuthTokenData authTokenData = new AuthTokenData(reissuedAccessToken, reissuedRefreshToken);
        given(authService.reissue(anyString(), anyString())).willReturn(authTokenData);

        //when, then
        mockMvc.perform(put("/api/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(reissuedAccessToken))
                .andExpect(jsonPath("$.refreshToken").value(reissuedRefreshToken))
                .andDo(print());
        then(authService).should(times(1)).reissue(accessToken, refreshToken);
    }

    @DisplayName("인증 로그아웃 컨트롤러 테스트")
    @Test
    void logout_test() throws Exception {
        //when, then
        mockMvc.perform(delete("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
