package com.project.foradhd.global.service;

import com.project.foradhd.global.enums.RedisKeyType;
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

    public Optional<Object> getValue(RedisKeyType redisKeyType, String id) {
        return getValue(redisKeyType.getKey(id));
    }

    public <T> Optional<T> getValue(RedisKeyType redisKeyType, String id, Class<T> clazz) {
        return getValue(redisKeyType, id)
                .map(obj -> clazz.isInstance(obj) ? clazz.cast(obj) : null);
    }

    @Transactional
    public void setValue(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }

    @Transactional
    public void setValue(RedisKeyType redisKeyType, String id, Object value, long timeout, TimeUnit unit) {
        setValue(redisKeyType.getKey(id), value, timeout, unit);
    }

    @Transactional
    public void setValue(String key, Object value, Duration timeout) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout);
    }

    @Transactional
    public void setValue(RedisKeyType redisKeyType, String id, Object value, Duration timeout) {
        setValue(redisKeyType.getKey(id), value, timeout);
    }

    @Transactional
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    @Transactional
    public void deleteValue(RedisKeyType redisKeyType, String id) {
        deleteValue(redisKeyType.getKey(id));
    }

    @Transactional
    public void expireValue(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Transactional
    public void expireValue(RedisKeyType redisKeyType, String id, long timeout, TimeUnit unit) {
        expireValue(redisKeyType.getKey(id), timeout, unit);
    }

    @Transactional
    public void expireValue(String key, Duration timeout) {
        redisTemplate.expire(key, timeout);
    }

    @Transactional
    public void expireValue(RedisKeyType redisKeyType, String id, Duration timeout) {
        expireValue(redisKeyType.getKey(id), timeout);
    }
}
