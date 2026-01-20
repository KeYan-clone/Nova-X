package com.novax.common.bff.aggregator;

/**
 * BFF 数据聚合器基类
 * 提供多服务调用聚合的通用逻辑
 */
public abstract class BaseAggregator {

    /**
     * 并行聚合多个服务的数据
     */
    protected <T> T parallelAggregate() {
        // TODO: 实现并行调用多个微服务的逻辑
        // 使用 CompletableFuture 并行调用
        return null;
    }

    /**
     * 串行聚合多个服务的数据
     */
    protected <T> T sequentialAggregate() {
        // TODO: 实现串行调用多个微服务的逻辑
        return null;
    }

    /**
     * 带降级的聚合
     */
    protected <T> T aggregateWithFallback() {
        // TODO: 实现带降级策略的聚合逻辑
        return null;
    }
}
