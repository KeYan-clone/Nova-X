package com.novax.common.log.filter;

import com.novax.common.log.utils.TraceIdGenerator;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * TraceID过滤器
 * 为每个HTTP请求生成或传递TraceID
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter implements Filter {

    /**
     * TraceID请求头名称
     */
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    /**
     * 请求ID请求头名称
     */
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    /**
     * 用户ID请求头名称
     */
    private static final String USER_ID_HEADER = "X-User-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            // 获取或生成TraceID
            String traceId = httpRequest.getHeader(TRACE_ID_HEADER);
            if (traceId == null || traceId.isEmpty()) {
                traceId = TraceIdGenerator.generate();
            }
            TraceIdGenerator.setTraceId(traceId);

            // 获取或生成请求ID
            String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
            if (requestId == null || requestId.isEmpty()) {
                requestId = TraceIdGenerator.generate();
            }
            TraceIdGenerator.setRequestId(requestId);

            // 获取用户ID（如果存在）
            String userId = httpRequest.getHeader(USER_ID_HEADER);
            if (userId != null && !userId.isEmpty()) {
                TraceIdGenerator.setUserId(userId);
            }

            // 将TraceID和RequestID添加到响应头
            httpResponse.setHeader(TRACE_ID_HEADER, traceId);
            httpResponse.setHeader(REQUEST_ID_HEADER, requestId);

            // 继续过滤链
            chain.doFilter(request, response);
        } finally {
            // 清理MDC上下文
            TraceIdGenerator.clear();
        }
    }
}
