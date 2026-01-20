package com.novax.gateway.filter;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * TraceId生成过滤器
 * 为每个请求生成唯一的TraceId，用于分布式链路追踪
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Component
@Slf4j
public class TraceIdFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 获取或生成TraceId
        String traceId = request.getHeaders().getFirst(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString(true);
        }

        // 生成RequestId
        String requestId = UUID.randomUUID().toString(true);

        // 将TraceId和RequestId添加到请求头
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(TRACE_ID_HEADER, traceId)
                .header(REQUEST_ID_HEADER, requestId)
                .build();

        // 将TraceId添加到响应头
        exchange.getResponse().getHeaders().add(TRACE_ID_HEADER, traceId);
        exchange.getResponse().getHeaders().add(REQUEST_ID_HEADER, requestId);

        log.debug("请求路径: {}, TraceId: {}, RequestId: {}",
                request.getPath().value(), traceId, requestId);

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        // TraceId过滤器优先级最高
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
