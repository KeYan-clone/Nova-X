package com.novax.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局异常处理器
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Configuration
@Order(-1)
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        log.error("网关异常: {}", ex.getMessage(), ex);

        // 设置响应头
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 处理不同类型的异常
        String message;
        HttpStatus status;

        if (ex instanceof NotFoundException) {
            message = "服务未找到";
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            message = responseStatusException.getReason();
            status = HttpStatus.resolve(responseStatusException.getStatusCode().value());
        } else {
            message = "网关内部错误";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response.setStatusCode(status);

        String body = String.format("""
            {
                "code": %d,
                "message": "%s",
                "data": null
            }
            """, status.value(), message);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
