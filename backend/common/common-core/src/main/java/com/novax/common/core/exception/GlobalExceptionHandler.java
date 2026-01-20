package com.novax.common.core.exception;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 业务异常
   */
  @ExceptionHandler(BusinessException.class)
  public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
    log.warn("Business exception: code={}, message={}, path={}",
        e.getCode(), e.getMessage(), request.getRequestURI());
    return Result.fail(e.getCode(), e.getMessage());
  }

  /**
   * 参数校验异常 - @Valid
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    FieldError fieldError = e.getBindingResult().getFieldError();
    String message = fieldError != null ? fieldError.getDefaultMessage() : "Parameter validation failed";
    log.warn("Parameter validation exception: {}", message);
    return Result.fail(ResultCode.PARAM_VALIDATION_ERROR, message);
  }

  /**
   * 参数校验异常 - @Validated
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<?> handleBindException(BindException e) {
    FieldError fieldError = e.getBindingResult().getFieldError();
    String message = fieldError != null ? fieldError.getDefaultMessage() : "Parameter binding failed";
    log.warn("Parameter binding exception: {}", message);
    return Result.fail(ResultCode.PARAM_VALIDATION_ERROR, message);
  }

  /**
   * 参数校验异常 - @Validated (path variable/request param)
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    String message = violations.isEmpty() ? "Constraint violation"
        : violations.iterator().next().getMessage();
    log.warn("Constraint violation exception: {}", message);
    return Result.fail(ResultCode.PARAM_VALIDATION_ERROR, message);
  }

  /**
   * 非法参数异常
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn("Illegal argument exception: {}", e.getMessage());
    return Result.fail(ResultCode.PARAM_ERROR, e.getMessage());
  }

  /**
   * 空指针异常
   */
  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Result<?> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
    log.error("Null pointer exception at path: {}", request.getRequestURI(), e);
    return Result.error("System error: null pointer exception");
  }

  /**
   * 其他未捕获异常
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Result<?> handleException(Exception e, HttpServletRequest request) {
    log.error("Unhandled exception at path: {}", request.getRequestURI(), e);
    return Result.error("System error: " + e.getMessage());
  }
}
