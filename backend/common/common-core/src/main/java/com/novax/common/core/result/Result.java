package com.novax.common.core.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一响应结果封装
 *
 * @param <T> 数据类型
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 响应码：0 成功，其他为错误码
   */
  private Integer code;

  /**
   * 响应消息
   */
  private String message;

  /**
   * 响应数据
   */
  private T data;

  /**
   * 响应时间戳
   */
  private LocalDateTime timestamp;

  /**
   * 链路追踪ID
   */
  private String traceId;

  public Result() {
    this.timestamp = LocalDateTime.now();
  }

  public Result(Integer code, String message) {
    this();
    this.code = code;
    this.message = message;
  }

  public Result(Integer code, String message, T data) {
    this(code, message);
    this.data = data;
  }

  /**
   * 成功响应（无数据）
   */
  public static <T> Result<T> success() {
    return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
  }

  /**
   * 成功响应（带数据）
   */
  public static <T> Result<T> success(T data) {
    return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
  }

  /**
   * 成功响应（自定义消息）
   */
  public static <T> Result<T> success(String message) {
    return new Result<>(ResultCode.SUCCESS.getCode(), message);
  }

  /**
   * 成功响应（自定义消息和数据）
   */
  public static <T> Result<T> success(String message, T data) {
    return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
  }

  /**
   * 失败响应
   */
  public static <T> Result<T> fail(Integer code, String message) {
    return new Result<>(code, message);
  }

  /**
   * 失败响应（使用结果码枚举）
   */
  public static <T> Result<T> fail(ResultCode resultCode) {
    return new Result<>(resultCode.getCode(), resultCode.getMessage());
  }

  /**
   * 失败响应（使用结果码枚举和自定义消息）
   */
  public static <T> Result<T> fail(ResultCode resultCode, String message) {
    return new Result<>(resultCode.getCode(), message);
  }

  /**
   * 参数错误
   */
  public static <T> Result<T> paramError(String message) {
    return new Result<>(ResultCode.PARAM_ERROR.getCode(), message);
  }

  /**
   * 未授权
   */
  public static <T> Result<T> unauthorized() {
    return fail(ResultCode.UNAUTHORIZED);
  }

  /**
   * 未授权（自定义消息）
   */
  public static <T> Result<T> unauthorized(String message) {
    return new Result<>(ResultCode.UNAUTHORIZED.getCode(), message);
  }

  /**
   * 权限不足
   */
  public static <T> Result<T> forbidden() {
    return fail(ResultCode.FORBIDDEN);
  }

  /**
   * 资源不存在
   */
  public static <T> Result<T> notFound() {
    return fail(ResultCode.NOT_FOUND);
  }

  /**
   * 资源不存在（自定义消息）
   */
  public static <T> Result<T> notFound(String message) {
    return new Result<>(ResultCode.NOT_FOUND.getCode(), message);
  }

  /**
   * 业务冲突
   */
  public static <T> Result<T> conflict(String message) {
    return new Result<>(ResultCode.CONFLICT.getCode(), message);
  }

  /**
   * 频率限制
   */
  public static <T> Result<T> tooManyRequests() {
    return fail(ResultCode.TOO_MANY_REQUESTS);
  }

  /**
   * 系统错误
   */
  public static <T> Result<T> error() {
    return fail(ResultCode.INTERNAL_SERVER_ERROR);
  }

  /**
   * 系统错误（自定义消息）
   */
  public static <T> Result<T> error(String message) {
    return new Result<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), message);
  }

  /**
   * 设置链路追踪ID
   */
  public Result<T> trace(String traceId) {
    this.traceId = traceId;
    return this;
  }

  /**
   * 判断是否成功
   */
  public boolean isSuccess() {
    return ResultCode.SUCCESS.getCode().equals(this.code);
  }
}
