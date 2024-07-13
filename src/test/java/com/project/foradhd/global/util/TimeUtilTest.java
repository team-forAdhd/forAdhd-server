package com.project.foradhd.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TimeUtil 테스트")
class TimeUtilTest {

    @DisplayName("LocalDateTime → Epoch Second 변환 테스트")
    @Test
    void to_epoch_second_test() {
        //given
        long epochSecond = 1720624981;
        LocalDateTime localDateTime = Instant.ofEpochSecond(epochSecond)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();

        //when
        long convertedEpochSecond = TimeUtil.toEpochSecond(localDateTime);

        //then
        assertThat(convertedEpochSecond).isEqualTo(epochSecond);
    }
}
