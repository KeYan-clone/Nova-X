package com.novax.common.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redis 分布式锁工具类
 */
@Slf4j
@Component
public class RedisLockUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加锁（默认30秒超时）
     */
    public RLock lock(String lockKey) {
        return lock(lockKey, 30, TimeUnit.SECONDS);
    }

    /**
     * 加锁（指定超时时间）
     */
    public RLock lock(String lockKey, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, unit);
        log.debug("Acquired lock: {}", lockKey);
        return lock;
    }

    /**
     * 尝试加锁
     */
    public boolean tryLock(String lockKey) {
        return tryLock(lockKey, 0, 30, TimeUnit.SECONDS);
    }

    /**
     * 尝试加锁（指定等待和超时时间）
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, unit);
            if (acquired) {
                log.debug("Acquired lock: {}", lockKey);
            } else {
                log.debug("Failed to acquire lock: {}", lockKey);
            }
            return acquired;
        } catch (InterruptedException e) {
            log.error("Lock interrupted: {}", lockKey, e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 解锁
     */
    public void unlock(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.debug("Released lock");
        }
    }

    /**
     * 解锁（通过key）
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        unlock(lock);
    }

    /**
     * 执行带锁的操作
     */
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        RLock lock = lock(lockKey);
        try {
            return supplier.get();
        } finally {
            unlock(lock);
        }
    }

    /**
     * 执行带锁的操作（无返回值）
     */
    public void executeWithLock(String lockKey, Runnable runnable) {
        RLock lock = lock(lockKey);
        try {
            runnable.run();
        } finally {
            unlock(lock);
        }
    }

    /**
     * 尝试执行带锁的操作
     */
    public <T> T tryExecuteWithLock(String lockKey, Supplier<T> supplier, T defaultValue) {
        if (tryLock(lockKey)) {
            RLock lock = redissonClient.getLock(lockKey);
            try {
                return supplier.get();
            } finally {
                unlock(lock);
            }
        }
        return defaultValue;
    }
}
