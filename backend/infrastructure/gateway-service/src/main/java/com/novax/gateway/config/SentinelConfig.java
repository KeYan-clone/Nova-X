package com.novax.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import jakarta.annotation.PostConstruct;

/**
 * Sentinel限流配置
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Configuration
@Slf4j
public class SentinelConfig {

    @PostConstruct
    public void init() {
        // 配置限流后的响应
        GatewayCallbackManager.setBlockHandler(new CustomBlockRequestHandler());
        log.info("Sentinel网关限流配置初始化完成");
    }

    /**
     * 自定义限流响应处理器
     */
    private static class CustomBlockRequestHandler implements BlockRequestHandler {

        @Override
        public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
            log.warn("请求被限流: {}, 原因: {}",
                    exchange.getRequest().getPath().value(),
                    t.getMessage());

            String json = """
                {
                    "code": 429,
                    "message": "请求过于频繁，请稍后重试",
                    "data": null
                }
                """;

            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(json);
        }
    }
}
