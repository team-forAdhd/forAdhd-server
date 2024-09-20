package com.project.foradhd.global.service;

import com.project.foradhd.global.enums.CacheKeyType;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheService {

    Optional<Object> getValue(String key);

    Optional<Object> getValue(CacheKeyType cacheKeyType, String id);

    <T> Optional<T> getValue(CacheKeyType cacheKeyType, String id, Class<T> clazz);

    void setValue(String key, Object value, long timeout, TimeUnit unit);

    void setValue(CacheKeyType cacheKeyType, String id, Object value, long timeout, TimeUnit unit);

    void setValue(String key, Object value, Duration timeout);

    void setValue(CacheKeyType cacheKeyType, String id, Object value, Duration timeout);

    void deleteValue(String key);

    void deleteValue(CacheKeyType cacheKeyType, String id);

    void expireValue(String key, long timeout, TimeUnit unit);

    void expireValue(CacheKeyType cacheKeyType, String id, long timeout, TimeUnit unit);

    void expireValue(String key, Duration timeout);

    void expireValue(CacheKeyType cacheKeyType, String id, Duration timeout);
}
