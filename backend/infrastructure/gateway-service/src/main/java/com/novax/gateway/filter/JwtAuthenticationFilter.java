package com.novax.gateway.filter;

import com.novax.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * JWT认证过滤器
 * 验证请求中的JWT Token
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    /**
     * 不需要认证的路径白名单
     */
    private static final List<String> WHITE_LIST = List.of(
        "/api/v1/auth/login",
        "/api/v1/auth/register",
        "/api/v1/auth/captcha",
        "/api/v1/auth/sms-code",
        "/doc.html",
        "/v3/api-docs",
        "/swagger-ui",
        "/webjars",
        "/favicon.ico"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 白名单路径直接放行
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 获取Token
        String token = extractToken(request);

        // Token为空，返回401
        if (token == null || token.isEmpty()) {
            log.warn("访问路径 {} 未携带Token", path);
            return unauthorized(exchange.getResponse(), "未授权，请先登录");
        }

        // 验证Token
        try {
            if (!jwtUtil.validateToken(token)) {
                log.warn("访问路径 {} 的Token无效", path);
                return unauthorized(exchange.getResponse(), "Token无效或已过期");
            }

            // 解析Token，获取用户信息
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Long userId = ((Number) claims.get("userId")).longValue();
            String username = (String) claims.get("username");
            String userType = (String) claims.get("userType");

            // 将用户信息添加到请求头，传递给下游服务
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-Username", username)
                    .header("X-User-Type", userType)
                    .build();

            log.debug("用户 {} 访问路径 {}", username, path);

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage());
            return unauthorized(exchange.getResponse(), "Token验证失败");
        }
    }

    /**
     * 从请求中提取Token
     */
    private String extractToken(ServerHttpRequest request) {
        // 1. 从Authorization头获取
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }

        // 2. 从URL参数获取（用于WebSocket等场景）
        List<String> tokenParams = request.getQueryParams().get("token");
        if (tokenParams != null && !tokenParams.isEmpty()) {
            return tokenParams.get(0);
        }

        return null;
    }

    /**
     * 判断路径是否在白名单中
     */
    private boolean isWhiteList(String path) {
        return WHITE_LIST.stream().anyMatch(path::startsWith);
    }

    /**
     * 返回401未授权响应
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        // 认证过滤器优先级设置为最高
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
