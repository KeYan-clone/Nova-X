package com.novax.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一结果码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

  // ========== 成功 ==========
  SUCCESS(0, "Success"),

  // ========== 客户端错误 (40xxx) ==========
  PARAM_ERROR(40001, "Parameter error"),
  PARAM_VALIDATION_ERROR(40002, "Parameter validation failed"),
  UNAUTHORIZED(40003, "Unauthorized"),
  INVALID_TOKEN(40004, "Invalid token"),
  IDEMPOTENCY_CONFLICT(40005, "Idempotency key conflict"),

  FORBIDDEN(40300, "Forbidden"),
  NOT_FOUND(40400, "Resource not found"),

  CONFLICT(40900, "Business conflict"),
  RESOURCE_ALREADY_EXISTS(40901, "Resource already exists"),
  INSUFFICIENT_BALANCE(40902, "Insufficient balance"),
  DEVICE_UNAVAILABLE(40903, "Device unavailable"),
  SESSION_NOT_ACTIVE(40904, "Charging session not active"),

  TOO_MANY_REQUESTS(42900, "Too many requests"),

  // ========== 服务端错误 (50xxx) ==========
  INTERNAL_SERVER_ERROR(50000, "Internal server error"),
  SERVICE_UNAVAILABLE(50300, "Service unavailable"),
  GATEWAY_TIMEOUT(50400, "Gateway timeout"),

  DATABASE_ERROR(50100, "Database error"),
  CACHE_ERROR(50200, "Cache error"),
  MQ_ERROR(50300, "Message queue error"),
  RPC_ERROR(50400, "Remote procedure call error"),
  EXTERNAL_API_ERROR(50500, "External API error"),

  // ========== 业务错误 (60xxx) ==========
  ACCOUNT_NOT_FOUND(60001, "Account not found"),
  ACCOUNT_DISABLED(60002, "Account disabled"),
  PASSWORD_ERROR(60003, "Password error"),
  VERIFICATION_CODE_ERROR(60004, "Verification code error"),

  STATION_NOT_FOUND(60101, "Station not found"),
  STATION_OFFLINE(60102, "Station offline"),

  DEVICE_NOT_FOUND(60201, "Device not found"),
  DEVICE_OFFLINE(60202, "Device offline"),
  DEVICE_FAULT(60203, "Device fault"),

  CONNECTOR_NOT_FOUND(60301, "Connector not found"),
  CONNECTOR_OCCUPIED(60302, "Connector occupied"),
  CONNECTOR_FAULT(60303, "Connector fault"),

  SESSION_NOT_FOUND(60401, "Charging session not found"),
  SESSION_ALREADY_STARTED(60402, "Charging session already started"),
  SESSION_CANNOT_STOP(60403, "Charging session cannot stop"),

  PAYMENT_FAILED(60501, "Payment failed"),
  PAYMENT_TIMEOUT(60502, "Payment timeout"),
  REFUND_FAILED(60503, "Refund failed"),

  PRICING_RULE_NOT_FOUND(60601, "Pricing rule not found"),

  INSUFFICIENT_POWER(60701, "Insufficient power"),
  SCHEDULING_CONFLICT(60702, "Scheduling conflict"),

  DR_ORDER_NOT_FOUND(60801, "DR order not found"),
  DR_ORDER_EXPIRED(60802, "DR order expired"),

  MEMBER_NOT_FOUND(60901, "Member not found"),
  MEMBER_EXPIRED(60902, "Member expired"),
  INSUFFICIENT_POINTS(60903, "Insufficient points"),

  WORK_ORDER_NOT_FOUND(61001, "Work order not found");

  /**
   * 响应码
   */
  private final Integer code;

  /**
   * 响应消息
   */
  private final String message;
}
