package com.novax.common.bff.exception;

import com.novax.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * BFF 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class BffExceptionHandler {

    /**
     * 参数校验异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        return Result.error(400, e.getMessage());
    }

    /**
     * Feign调用异常
     */
    @ExceptionHandler(feign.FeignException.class)
    public Result<?> handleFeignException(feign.FeignException e) {
        log.error("服务调用失败: {}", e.getMessage());
        return Result.error(503, "服务暂时不可用，请稍后重试");
    }

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(500, "系统繁忙，请稍后重试");
    }
}
