package com.project.foradhd.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StringUtil 테스트")
class StringUtilTest {

    @DisplayName("null 체크 후 앞뒤 문자열 공백 제거 테스트 - null인 경우")
    @Test
    void null_check_strip_with_null_test() {
        //given
        String value = null;

        //when
        String result = StringUtil.strip(value);

        //then
        assertThat(result).isNull();;
    }

    @DisplayName("null 체크 후 앞뒤 문자열 공백 제거 테스트 - null 아닌 경우")
    @Test
    void null_check_strip_with_not_null_test() {
        //given
        String value = "   hello world!     ";

        //when
        String result = StringUtil.strip(value);

        //then
        assertThat(result).isEqualTo("hello world!");
    }

    @DisplayName("null 체크 후 앞뒤 문자열 공백 제거 테스트 - 공백 문자열인 경우")
    @Test
    void null_check_strip_with_blank_test() {
        //given
        String value = "        ";

        //when
        String result = StringUtil.strip(value);

        //then
        assertThat(result).isEmpty();
    }

    @DisplayName("null 체크 후 prefix 추가 테스트 - null인 경우")
    @Test
    void null_check_add_prefix_with_null_test() {
        //given
        String value = null;
        String prefix = "prefix";

        //when
        String result = StringUtil.addPrefix(value, prefix);

        //then
        assertThat(result).isNull();
    }

    @DisplayName("null 체크 후 prefix 추가 테스트 - 이미 prefix 있는 경우")
    @Test
    void null_check_add_prefix_with_prefix_test() {
        //given
        String value = "prefix hello world!";
        String prefix = "prefix";

        //when
        String result = StringUtil.addPrefix(value, prefix);

        //then
        assertThat(result).isEqualTo(value);
    }

    @DisplayName("null 체크 후 prefix 추가 테스트 - prefix 없는 경우")
    @Test
    void null_check_add_prefix_without_prefix_test() {
        //given
        String value = "hello world!";
        String prefix = "prefix";

        //when
        String result = StringUtil.addPrefix(value, prefix);

        //then
        assertThat(result).isEqualTo("prefixhello world!");
    }
}
