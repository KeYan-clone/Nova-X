package com.novax.common.log.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceId 过滤器
 * 为每个请求生成唯一的 TraceId，用于链路追踪
 *
 * @author Nova-X
 * @since 2026-01-25
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter implements Filter {

    private static final String TRACE_ID = "traceId";
    private static final String HEADER_TRACE_ID = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 尝试从请求头获取 TraceId
        String traceId = httpRequest.getHeader(HEADER_TRACE_ID);

        // 如果请求头没有 TraceId，则生成新的
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
        }

        try {
            // 将 TraceId 放入 MDC，使其在日志中可用
            MDC.put(TRACE_ID, traceId);
            chain.doFilter(request, response);
        } finally {
            // 清理 MDC
            MDC.remove(TRACE_ID);
        }
    }

    /**
     * 生成 TraceId
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
