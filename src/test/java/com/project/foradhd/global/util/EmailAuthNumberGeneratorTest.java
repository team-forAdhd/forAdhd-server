package com.project.foradhd.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("이메일 인증번호 Generator 테스트")
class EmailAuthNumberGeneratorTest {

    static final int ALLOWED_MAX_DUPLICATED_TIMES = 2;

    @DisplayName("생성된 인증번호 내 숫자는 최대 2번만 중복")
    @Test
    void email_auth_number_generator_test() {
        //when
        String emailAuthNumber = EmailAuthNumberGenerator.generate();
        int[] numberCheckTable = new int[10];
        for (char numberChar : emailAuthNumber.toCharArray()) {
            numberCheckTable[numberChar - '0']++;
        }

        //then
        //AssertionError 발생 시에도 모든 Assertions 실행
        assertAll(
                () -> assertThat(numberCheckTable[0]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[1]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[2]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[3]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[4]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[5]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[6]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[7]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[8]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES),
                () -> assertThat(numberCheckTable[9]).isLessThanOrEqualTo(ALLOWED_MAX_DUPLICATED_TIMES)
        );
    }
}
