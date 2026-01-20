package com.novax.common.log.utils;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;

/**
 * TraceID生成器
 * 用于分布式链路追踪，生成全局唯一的TraceID
 *
 * @author Nova-X
 * @since 2026-01-20
 */
public class TraceIdGenerator {

    /**
     * TraceID在MDC中的键
     */
    public static final String TRACE_ID_KEY = "traceId";

    /**
     * 请求ID在MDC中的键
     */
    public static final String REQUEST_ID_KEY = "requestId";

    /**
     * 用户ID在MDC中的键
     */
    public static final String USER_ID_KEY = "userId";

    /**
     * 生成TraceID
     *
     * @return 32位UUID字符串
     */
    public static String generate() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 设置TraceID到MDC
     *
     * @param traceId TraceID
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 获取当前TraceID
     *
     * @return TraceID，如果不存在则返回null
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 移除TraceID
     */
    public static void removeTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    /**
     * 设置请求ID到MDC
     *
     * @param requestId 请求ID
     */
    public static void setRequestId(String requestId) {
        MDC.put(REQUEST_ID_KEY, requestId);
    }

    /**
     * 获取当前请求ID
     *
     * @return 请求ID，如果不存在则返回null
     */
    public static String getRequestId() {
        return MDC.get(REQUEST_ID_KEY);
    }

    /**
     * 移除请求ID
     */
    public static void removeRequestId() {
        MDC.remove(REQUEST_ID_KEY);
    }

    /**
     * 设置用户ID到MDC
     *
     * @param userId 用户ID
     */
    public static void setUserId(String userId) {
        MDC.put(USER_ID_KEY, userId);
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，如果不存在则返回null
     */
    public static String getUserId() {
        return MDC.get(USER_ID_KEY);
    }

    /**
     * 移除用户ID
     */
    public static void removeUserId() {
        MDC.remove(USER_ID_KEY);
    }

    /**
     * 清除所有MDC上下文
     */
    public static void clear() {
        MDC.clear();
    }
}
