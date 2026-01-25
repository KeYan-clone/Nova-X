package com.novax.common.bff.exception;

import com.novax.common.bff.response.BffResponse;
import com.novax.common.core.result.Result;
import com.novax.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * BFF 全局异常处理器
 * 统一处理 BFF 层的各种异常，返回标准化的错误响应
 */
@Slf4j
@RestControllerAdvice
public class BffExceptionHandler {

    /**
     * 参数校验异常（IllegalArgumentException）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        return BffResponse.paramError(e.getMessage());
    }

    /**
     * 参数校验异常（@Valid 注解触发）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数验证失败: {}", message);
        return BffResponse.validationError(message);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定失败: {}", message);
        return BffResponse.validationError(message);
    }

    /**
     * 约束违反异常（@Validated 注解触发）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("约束验证失败: {}", message);
        return BffResponse.validationError(message);
    }

    /**
     * Feign 调用异常 - 400 Bad Request
     */
    @ExceptionHandler(feign.FeignException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleFeignBadRequest(feign.FeignException.BadRequest e) {
        log.warn("下游服务参数错误: {}", e.getMessage());
        return BffResponse.paramError("请求参数错误");
    }

    /**
     * Feign 调用异常 - 401 Unauthorized
     */
    @ExceptionHandler(feign.FeignException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleFeignUnauthorized(feign.FeignException.Unauthorized e) {
        log.warn("下游服务认证失败: {}", e.getMessage());
        return BffResponse.unauthorized();
    }

    /**
     * Feign 调用异常 - 403 Forbidden
     */
    @ExceptionHandler(feign.FeignException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<?> handleFeignForbidden(feign.FeignException.Forbidden e) {
        log.warn("下游服务权限不足: {}", e.getMessage());
        return BffResponse.forbidden();
    }

    /**
     * Feign 调用异常 - 404 Not Found
     */
    @ExceptionHandler(feign.FeignException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> handleFeignNotFound(feign.FeignException.NotFound e) {
        log.warn("下游服务资源不存在: {}", e.getMessage());
        return BffResponse.notFound("资源不存在");
    }

    /**
     * Feign 调用异常 - 503 Service Unavailable
     */
    @ExceptionHandler(feign.FeignException.ServiceUnavailable.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Result<?> handleFeignServiceUnavailable(feign.FeignException.ServiceUnavailable e) {
        log.error("下游服务不可用: {}", e.getMessage());
        return BffResponse.serviceUnavailable("服务暂时不可用，请稍后重试");
    }

    /**
     * Feign 调用异常 - 通用
     */
    @ExceptionHandler(feign.FeignException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Result<?> handleFeignException(feign.FeignException e) {
        log.error("服务调用失败: status={}, message={}", e.status(), e.getMessage());

        // 根据 HTTP 状态码返回不同的错误
        if (e.status() >= 500) {
            return BffResponse.serviceUnavailable("服务暂时不可用，请稍后重试");
        } else if (e.status() == 404) {
            return BffResponse.notFound("资源不存在");
        } else if (e.status() == 403) {
            return BffResponse.forbidden();
        } else if (e.status() == 401) {
            return BffResponse.unauthorized();
        } else {
            return BffResponse.error("服务调用失败");
        }
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常: ", e);
        return BffResponse.error("系统内部错误");
    }

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: ", e);
        return BffResponse.error("系统繁忙，请稍后重试");
    }
}
