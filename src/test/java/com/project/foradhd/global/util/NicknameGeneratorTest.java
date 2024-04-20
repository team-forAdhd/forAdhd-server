package com.project.foradhd.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("닉네임 자동 생성기 테스트 (일단 현재 쓸 일 없음)")
class NicknameGeneratorTest {

    private static final String NICKNAME_REGEX = "[ㄱ-ㅣ가-힣]+\\s[ㄱ-ㅣ가-힣]+\\d";

    @DisplayName("생성되는 닉네임 '형용사' + ' ' + '이름' + '숫자' 형식 테스트")
    @Test
    void nickname_format_test() {
        //given, when
        String nickname = NicknameGenerator.generate();

        //then
        assertThat(nickname).matches(NICKNAME_REGEX);
    }
}
