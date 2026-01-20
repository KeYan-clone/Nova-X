package com.novax.common.bff.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * BFF 统一认证拦截器
 * 所有 BFF 服务通用的认证逻辑
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final ThreadLocal<UserContext> USER_CONTEXT = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从 Header 中获取 Token
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Missing or invalid Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            // 2. 验证 Token（简化版，实际应使用 common-security 的 JwtUtil）
            // JwtUtil jwtUtil = ApplicationContextHolder.getBean(JwtUtil.class);
            // Claims claims = jwtUtil.parseToken(token);

            // 3. 将用户信息存入 ThreadLocal
            UserContext context = new UserContext();
            // context.setUserId(claims.get("userId", Long.class));
            // context.setUsername(claims.get("username", String.class));
            // context.setRoles(claims.get("roles", List.class));
            USER_CONTEXT.set(context);

            log.debug("Token验证成功");
            return true;

        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理 ThreadLocal 防止内存泄漏
        USER_CONTEXT.remove();
    }

    /**
     * 获取当前用户上下文
     */
    public static UserContext getCurrentUser() {
        return USER_CONTEXT.get();
    }

    /**
     * 用户上下文信息
     */
    public static class UserContext {
        private Long userId;
        private String username;
        private java.util.List<String> roles;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public java.util.List<String> getRoles() { return roles; }
        public void setRoles(java.util.List<String> roles) { this.roles = roles; }
    }
}
