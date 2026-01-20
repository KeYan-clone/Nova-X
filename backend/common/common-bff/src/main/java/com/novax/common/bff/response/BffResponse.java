package com.novax.common.bff.response;

import com.novax.common.core.domain.Result;

/**
 * BFF 统一响应封装
 * 提供 BFF 层特有的响应格式处理
 */
public class BffResponse {

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 业务错误响应
     */
    public static <T> Result<T> error(String message) {
        return Result.error(message);
    }

    /**
     * 带错误码的响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        return Result.error(code, message);
    }
}
