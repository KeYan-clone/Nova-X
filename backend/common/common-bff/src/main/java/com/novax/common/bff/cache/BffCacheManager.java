package com.novax.common.bff.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * BFF 缓存管理器
 * 统一管理 BFF 层的缓存逻辑，提供缓存穿透、缓存失效等场景的处理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BffCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String BFF_CACHE_PREFIX = "bff:";
    private static final long DEFAULT_EXPIRE_SECONDS = 300; // 5分钟
    private static final long SHORT_EXPIRE_SECONDS = 60; // 1分钟
    private static final long LONG_EXPIRE_SECONDS = 3600; // 1小时

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(buildKey(key));
            if (value == null) {
                return null;
            }

            // 如果类型匹配，直接返回
            if (type.isInstance(value)) {
                return (T) value;
            }

            // 否则尝试转换
            return objectMapper.convertValue(value, type);
        } catch (Exception e) {
            log.error("获取缓存失败: key={}", key, e);
            return null;
        }
    }

    /**
     * 获取缓存（支持泛型）
     */
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            Object value = redisTemplate.opsForValue().get(buildKey(key));
            if (value == null) {
                return null;
            }
            return objectMapper.convertValue(value, typeReference);
        } catch (Exception e) {
            log.error("获取缓存失败: key={}", key, e);
            return null;
        }
    }

    /**
     * 获取缓存，如果不存在则执行 supplier 并缓存结果
     */
    public <T> T getOrLoad(String key, Class<T> type, Supplier<T> supplier) {
        return getOrLoad(key, type, supplier, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 获取缓存，如果不存在则执行 supplier 并缓存结果（指定过期时间）
     */
    public <T> T getOrLoad(String key, Class<T> type, Supplier<T> supplier, long expireSeconds) {
        T cached = get(key, type);
        if (cached != null) {
            return cached;
        }

        // 加载数据
        T data = supplier.get();
        if (data != null) {
            set(key, data, expireSeconds);
        }
        return data;
    }

    /**
     * 设置缓存（默认5分钟过期）
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 设置缓存（短时间过期，1分钟）
     */
    public void setShort(String key, Object value) {
        set(key, value, SHORT_EXPIRE_SECONDS);
    }

    /**
     * 设置缓存（长时间过期，1小时）
     */
    public void setLong(String key, Object value) {
        set(key, value, LONG_EXPIRE_SECONDS);
    }

    /**
     * 设置缓存（指定过期时间，秒）
     */
    public void set(String key, Object value, long expireSeconds) {
        try {
            redisTemplate.opsForValue().set(
                    buildKey(key),
                    value,
                    expireSeconds,
                    TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("设置缓存失败: key={}", key, e);
        }
    }

    /**
     * 设置缓存（指定过期时间，Duration）
     */
    public void set(String key, Object value, Duration duration) {
        try {
            redisTemplate.opsForValue().set(
                    buildKey(key),
                    value,
                    duration);
        } catch (Exception e) {
            log.error("设置缓存失败: key={}", key, e);
        }
    }

    /**
     * 删除缓存
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(buildKey(key));
        } catch (Exception e) {
            log.error("删除缓存失败: key={}", key, e);
        }
    }

    /**
     * 批量删除缓存
     */
    public void delete(Collection<String> keys) {
        try {
            List<String> fullKeys = keys.stream()
                    .map(this::buildKey)
                    .toList();
            redisTemplate.delete(fullKeys);
        } catch (Exception e) {
            log.error("批量删除缓存失败: keys={}", keys, e);
        }
    }

    /**
     * 删除匹配的缓存（支持通配符）
     */
    public void deletePattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(buildKey(pattern));
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.error("删除匹配缓存失败: pattern={}", pattern, e);
        }
    }

    /**
     * 检查缓存是否存在
     */
    public boolean exists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(key)));
        } catch (Exception e) {
            log.error("检查缓存是否存在失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 设置缓存过期时间
     */
    public boolean expire(String key, long expireSeconds) {
        try {
            return Boolean.TRUE.equals(redisTemplate.expire(buildKey(key), expireSeconds, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.error("设置缓存过期时间失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 获取缓存剩余过期时间（秒）
     */
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(buildKey(key), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("获取缓存过期时间失败: key={}", key, e);
            return null;
        }
    }

    /**
     * Hash 操作 - 设置
     */
    public void hSet(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(buildKey(key), field, value);
        } catch (Exception e) {
            log.error("Hash设置失败: key={}, field={}", key, field, e);
        }
    }

    /**
     * Hash 操作 - 获取
     */
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String field, Class<T> type) {
        try {
            Object value = redisTemplate.opsForHash().get(buildKey(key), field);
            if (value == null) {
                return null;
            }
            if (type.isInstance(value)) {
                return (T) value;
            }
            return objectMapper.convertValue(value, type);
        } catch (Exception e) {
            log.error("Hash获取失败: key={}, field={}", key, field, e);
            return null;
        }
    }

    /**
     * Hash 操作 - 获取所有
     */
    public Map<Object, Object> hGetAll(String key) {
        try {
            return redisTemplate.opsForHash().entries(buildKey(key));
        } catch (Exception e) {
            log.error("Hash获取所有失败: key={}", key, e);
            return Map.of();
        }
    }

    /**
     * Hash 操作 - 删除字段
     */
    public void hDelete(String key, Object... fields) {
        try {
            redisTemplate.opsForHash().delete(buildKey(key), fields);
        } catch (Exception e) {
            log.error("Hash删除字段失败: key={}", key, e);
        }
    }

    /**
     * 构建完整的缓存 key
     */
    private String buildKey(String key) {
        return BFF_CACHE_PREFIX + key;
    }
}
