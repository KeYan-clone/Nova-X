package com.novax.common.bff.aggregator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * BFF 数据聚合器基类
 * 提供多服务调用聚合的通用逻辑
 */
@Slf4j
public abstract class BaseAggregator {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 并行聚合多个服务的数据
     * @param suppliers 多个服务调用的 Supplier
     * @return 聚合结果列表
     */
    @SafeVarargs
    protected final <T> List<T> parallelAggregate(Supplier<T>... suppliers) {
        List<CompletableFuture<T>> futures = new ArrayList<>();

        for (Supplier<T> supplier : suppliers) {
            CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier, EXECUTOR);
            futures.add(future);
        }

        // 等待所有调用完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        try {
            allOf.join();

            List<T> results = new ArrayList<>();
            for (CompletableFuture<T> future : futures) {
                results.add(future.get());
            }
            return results;

        } catch (Exception e) {
            log.error("并行聚合失败: {}", e.getMessage());
            throw new RuntimeException("数据聚合失败", e);
        }
    }

    /**
     * 串行聚合多个服务的数据
     * @param suppliers 多个服务调用的 Supplier（按顺序执行）
     * @return 聚合结果列表
     */
    @SafeVarargs
    protected final <T> List<T> sequentialAggregate(Supplier<T>... suppliers) {
        List<T> results = new ArrayList<>();

        for (Supplier<T> supplier : suppliers) {
            try {
                T result = supplier.get();
                results.add(result);
            } catch (Exception e) {
                log.error("串行聚合失败: {}", e.getMessage());
                throw new RuntimeException("数据聚合失败", e);
            }
        }

        return results;
    }

    /**
     * 带降级的聚合
     * @param supplier 主服务调用
     * @param fallback 降级逻辑
     * @return 聚合结果
     */
    protected <T> T aggregateWithFallback(Supplier<T> supplier, Supplier<T> fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.warn("主调用失败，执行降级逻辑: {}", e.getMessage());
            try {
                return fallback.get();
            } catch (Exception fallbackEx) {
                log.error("降级逻辑也失败: {}", fallbackEx.getMessage());
                throw new RuntimeException("数据聚合失败且降级失败", fallbackEx);
            }
        }
    }

    /**
     * 并行聚合，允许部分失败
     * @param suppliers 多个服务调用的 Supplier
     * @return 成功的结果列表（失败的会被跳过）
     */
    @SafeVarargs
    protected final <T> List<T> parallelAggregateAllowPartialFailure(Supplier<T>... suppliers) {
        List<CompletableFuture<T>> futures = new ArrayList<>();

        for (Supplier<T> supplier : suppliers) {
            CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier, EXECUTOR)
                .exceptionally(ex -> {
                    log.warn("部分调用失败: {}", ex.getMessage());
                    return null;
                });
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        allOf.join();

        List<T> results = new ArrayList<>();
        for (CompletableFuture<T> future : futures) {
            try {
                T result = future.get();
                if (result != null) {
                    results.add(result);
                }
            } catch (Exception e) {
                log.warn("获取结果失败: {}", e.getMessage());
            }
        }

        return results;
    }
}
