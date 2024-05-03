package com.project.foradhd.global.service;

import com.project.foradhd.global.config.RedisConfig;
import com.project.foradhd.tags.IntegrationTag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@IntegrationTag
@DisplayName("Redis 서비스 통합 테스트")
@ExtendWith(SpringExtension.class)
@Import({RedisProperties.class, RedisConfig.class, RedisService.class})
class RedisServiceTest {

    @Autowired
    RedisService redisService;

    static final String KEY = "Spring";
    static final String VALUE = "Hello World";
    static final Duration TIMEOUT = Duration.ofSeconds(5);

    @BeforeEach
    void init() {
        redisService.setValue(KEY, VALUE, TIMEOUT);
    }

    @AfterEach
    void tearDown() {
        redisService.deleteValue(KEY);
    }

    @DisplayName("Redis에 데이터 저장 후 조회")
    @Test
    void get_value_test() {
        //when
        Optional<Object> value = redisService.getValue(KEY);

        //then
        assertThat(value).contains(VALUE);
    }

    @DisplayName("Redis 데이터 삭제 후 조회")
    @Test
    void get_empty_value_test() {
        //given
        redisService.deleteValue(KEY);

        //when
        Optional<Object> value = redisService.getValue(KEY);

        //then
        assertThat(value).isEmpty();
    }

    @DisplayName("Redis에 저장 후 만료된 데이터 조회")
    @Test
    void get_expired_value_test() {
        //지연 테스트 위한 Awaitility api 사용 (최대 10초 동안 100ms 마다 조건 만족 여부 체크)
        await().atMost(10, TimeUnit.SECONDS)
                .with()
                .pollDelay(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    Optional<Object> value = redisService.getValue(KEY);
                    assertThat(value).isEmpty();
                }
        );
    }

    @DisplayName("Redis에 저장된 데이터 수정")
    @Test
    void update_value_test() {
        //given
        String updatedValue = "Hi World";
        redisService.setValue(KEY, updatedValue, TIMEOUT);

        //when
        Optional<Object> value = redisService.getValue(KEY);

        //then
        assertThat(value).contains(updatedValue);
    }

    @DisplayName("Redis에 저장된 데이터 만료")
    @Test
    void expire_value_test() {
        //given
        Duration expiredTimeout = Duration.ofSeconds(0);
        redisService.expireValue(KEY, expiredTimeout);

        //when
        Optional<Object> value = redisService.getValue(KEY);

        //then
        assertThat(value).isEmpty();
    }
}
