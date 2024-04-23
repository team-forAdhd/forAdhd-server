package com.project.foradhd.global.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UUID Generator 테스트")
class UUIDGeneratorTest {

    static UUIDGenerator uuidGenerator;

    @BeforeAll
    static void initAll() {
        uuidGenerator = new UUIDGenerator();
    }

    @DisplayName("생성된 UUID 길이 32자 테스트")
    @Test
    void uuid_length_test() {
        //given, when
        String uuid = (String) uuidGenerator.generate(null, null);

        //then
        Assertions.assertThat(uuid).hasSize(32);
    }
}
