package com.project.foradhd.global.service.impl;

import com.project.foradhd.global.enums.CacheKeyType;
import com.project.foradhd.global.service.CacheService;
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
public class RedisService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<Object> getValue(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if (operations.get(key) == null) {
            return Optional.empty();
        }
        return Optional.of(operations.get(key));
    }

    @Override
    public Optional<Object> getValue(CacheKeyType cacheKeyType, String id) {
        return getValue(cacheKeyType.getKey(id));
    }

    @Override
    public <T> Optional<T> getValue(CacheKeyType cacheKeyType, String id, Class<T> clazz) {
        return getValue(cacheKeyType, id)
                .map(obj -> clazz.isInstance(obj) ? clazz.cast(obj) : null);
    }

    @Override
    @Transactional
    public void setValue(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }

    @Override
    @Transactional
    public void setValue(CacheKeyType cacheKeyType, String id, Object value, long timeout, TimeUnit unit) {
        setValue(cacheKeyType.getKey(id), value, timeout, unit);
    }

    @Override
    @Transactional
    public void setValue(String key, Object value, Duration timeout) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout);
    }

    @Override
    @Transactional
    public void setValue(CacheKeyType cacheKeyType, String id, Object value, Duration timeout) {
        setValue(cacheKeyType.getKey(id), value, timeout);
    }

    @Override
    @Transactional
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    @Override
    @Transactional
    public void deleteValue(CacheKeyType cacheKeyType, String id) {
        deleteValue(cacheKeyType.getKey(id));
    }

    @Override
    @Transactional
    public void expireValue(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    @Transactional
    public void expireValue(CacheKeyType cacheKeyType, String id, long timeout, TimeUnit unit) {
        expireValue(cacheKeyType.getKey(id), timeout, unit);
    }

    @Override
    @Transactional
    public void expireValue(String key, Duration timeout) {
        redisTemplate.expire(key, timeout);
    }

    @Override
    @Transactional
    public void expireValue(CacheKeyType cacheKeyType, String id, Duration timeout) {
        expireValue(cacheKeyType.getKey(id), timeout);
    }
}
