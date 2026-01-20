package com.novax.common.bff.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BFF 统一认证拦截器
 * 所有 BFF 服务通用的认证逻辑
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // TODO: 实现统一的 Token 验证逻辑
        // 1. 从 Header 中获取 Token
        // 2. 调用 common-security 验证 Token
        // 3. 将用户信息存入 ThreadLocal
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // TODO: 清理 ThreadLocal
    }
}
