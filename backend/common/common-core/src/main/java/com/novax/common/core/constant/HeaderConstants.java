package com.novax.common.core.constant;

/**
 * HTTP 头常量
 */
public interface HeaderConstants {

  /**
   * 认证头
   */
  String AUTHORIZATION = "Authorization";

  /**
   * Bearer Token 前缀
   */
  String BEARER_PREFIX = "Bearer ";

  /**
   * 链路追踪ID
   */
  String TRACE_ID = "X-Trace-Id";

  /**
   * API 版本
   */
  String API_VERSION = "X-API-Version";

  /**
   * 幂等键
   */
  String IDEMPOTENCY_KEY = "Idempotency-Key";

  /**
   * 客户端ID
   */
  String CLIENT_ID = "X-Client-Id";

  /**
   * 设备ID
   */
  String DEVICE_ID = "X-Device-Id";

  /**
   * 用户代理
   */
  String USER_AGENT = "User-Agent";

  /**
   * 真实IP
   */
  String X_REAL_IP = "X-Real-IP";

  /**
   * 转发IP
   */
  String X_FORWARDED_FOR = "X-Forwarded-For";

  /**
   * 时间戳
   */
  String TIMESTAMP = "X-Timestamp";

  /**
   * 随机数
   */
  String NONCE = "X-Nonce";

  /**
   * 签名
   */
  String SIGNATURE = "X-Signature";
}
