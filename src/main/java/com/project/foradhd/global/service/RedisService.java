package com.project.foradhd.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Optional<Object> getValue(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if (operations.get(key) == null) {
            return Optional.empty();
        }
        return Optional.of(operations.get(key));
    }

    @Transactional
    public void setValue(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }

    @Transactional
    public void setValue(String key, String value, Duration timeout) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout);
    }

    @Transactional
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    @Transactional
    public void expireValue(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Transactional
    public void expireValue(String key, Duration timeout) {
        redisTemplate.expire(key, timeout);
    }
}
