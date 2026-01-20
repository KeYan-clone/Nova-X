package com.novax.common.bff.controller;

import com.novax.common.bff.interceptor.AuthInterceptor;
import com.novax.common.bff.validator.ParamValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BFF 控制器基类
 * 所有 BFF 服务的 Controller 继承此类
 */
public abstract class BaseBffController {

    @Autowired
    protected ParamValidator paramValidator;

    /**
     * 获取当前登录用户ID
     */
    protected Long getCurrentUserId() {
        AuthInterceptor.UserContext context = AuthInterceptor.getCurrentUser();
        return context != null ? context.getUserId() : null;
    }

    /**
     * 获取当前登录用户名
     */
    protected String getCurrentUsername() {
        AuthInterceptor.UserContext context = AuthInterceptor.getCurrentUser();
        return context != null ? context.getUsername() : null;
    }

    /**
     * 获取当前用户角色列表
     */
    protected java.util.List<String> getCurrentUserRoles() {
        AuthInterceptor.UserContext context = AuthInterceptor.getCurrentUser();
        return context != null ? context.getRoles() : null;
    }

    /**
     * 检查当前用户是否有指定角色
     */
    protected boolean hasRole(String role) {
        java.util.List<String> roles = getCurrentUserRoles();
        return roles != null && roles.contains(role);
    }
}
