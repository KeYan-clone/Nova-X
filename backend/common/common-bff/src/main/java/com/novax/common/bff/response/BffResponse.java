package com.novax.common.bff.response;

import com.novax.common.core.result.Result;
import com.novax.common.core.result.ResultCode;

/**
 * BFF 统一响应封装
 * 提供 BFF 层特有的响应格式处理
 */
public class BffResponse {

    // ========== 成功响应 ==========

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message) {
        return Result.success(message);
    }

    /**
     * 成功响应（自定义消息和数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return Result.success(message, data);
    }

    // ========== 客户端错误响应 ==========

    /**
     * 参数错误
     */
    public static <T> Result<T> paramError(String message) {
        return Result.paramError(message);
    }

    /**
     * 参数验证失败
     */
    public static <T> Result<T> validationError(String message) {
        return Result.fail(ResultCode.PARAM_VALIDATION_ERROR, message);
    }

    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized() {
        return Result.unauthorized();
    }

    /**
     * 未授权（自定义消息）
     */
    public static <T> Result<T> unauthorized(String message) {
        return Result.unauthorized(message);
    }

    /**
     * Token 无效
     */
    public static <T> Result<T> invalidToken() {
        return Result.fail(ResultCode.INVALID_TOKEN);
    }

    /**
     * 权限不足
     */
    public static <T> Result<T> forbidden() {
        return Result.forbidden();
    }

    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound() {
        return Result.notFound();
    }

    /**
     * 资源不存在（自定义消息）
     */
    public static <T> Result<T> notFound(String message) {
        return Result.notFound(message);
    }

    /**
     * 业务冲突
     */
    public static <T> Result<T> conflict(String message) {
        return Result.conflict(message);
    }

    /**
     * 资源已存在
     */
    public static <T> Result<T> resourceExists(String message) {
        return Result.fail(ResultCode.RESOURCE_ALREADY_EXISTS, message);
    }

    /**
     * 频率限制
     */
    public static <T> Result<T> tooManyRequests() {
        return Result.tooManyRequests();
    }

    // ========== 服务端错误响应 ==========

    /**
     * 系统错误
     */
    public static <T> Result<T> error() {
        return Result.error();
    }

    /**
     * 系统错误（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return Result.error(message);
    }

    /**
     * 服务不可用
     */
    public static <T> Result<T> serviceUnavailable() {
        return Result.fail(ResultCode.SERVICE_UNAVAILABLE);
    }

    /**
     * 服务不可用（自定义消息）
     */
    public static <T> Result<T> serviceUnavailable(String message) {
        return Result.fail(ResultCode.SERVICE_UNAVAILABLE, message);
    }

    /**
     * 网关超时
     */
    public static <T> Result<T> gatewayTimeout() {
        return Result.fail(ResultCode.GATEWAY_TIMEOUT);
    }

    // ========== 业务错误响应 ==========

    /**
     * 使用结果码枚举返回错误
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return Result.fail(resultCode);
    }

    /**
     * 使用结果码枚举和自定义消息返回错误
     */
    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return Result.fail(resultCode, message);
    }

    /**
     * 使用错误码和消息返回错误
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return Result.fail(code, message);
    }
}
