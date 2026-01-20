package com.novax.common.bff.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * BFF 缓存管理器
 * 统一管理 BFF 层的缓存逻辑
 */
@Component
@RequiredArgsConstructor
public class BffCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BFF_CACHE_PREFIX = "bff:";
    private static final long DEFAULT_EXPIRE_SECONDS = 300; // 5分钟

    /**
     * 获取缓存
     */
    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(BFF_CACHE_PREFIX + key);
        return type.cast(value);
    }

    /**
     * 设置缓存（默认5分钟过期）
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 设置缓存（指定过期时间）
     */
    public void set(String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(
            BFF_CACHE_PREFIX + key,
            value,
            expireSeconds,
            TimeUnit.SECONDS
        );
    }

    /**
     * 删除缓存
     */
    public void delete(String key) {
        redisTemplate.delete(BFF_CACHE_PREFIX + key);
    }

    /**
     * 检查缓存是否存在
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BFF_CACHE_PREFIX + key));
    }
}
