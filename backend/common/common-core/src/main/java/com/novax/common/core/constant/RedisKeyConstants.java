package com.novax.common.core.constant;

/**
 * Redis 键常量
 */
public interface RedisKeyConstants {

  /**
   * Token 前缀
   */
  String TOKEN_PREFIX = "token:";

  /**
   * 验证码前缀
   */
  String VERIFY_CODE_PREFIX = "verify:code:";

  /**
   * 会话前缀
   */
  String SESSION_PREFIX = "session:";

  /**
   * 站点快照前缀
   */
  String STATION_SNAPSHOT_PREFIX = "station:snapshot:";

  /**
   * 设备影子前缀
   */
  String DEVICE_SHADOW_PREFIX = "device:shadow:";

  /**
   * 价格策略前缀
   */
  String PRICING_RULE_PREFIX = "pricing:rule:";

  /**
   * 用户限流前缀
   */
  String RATE_LIMIT_USER_PREFIX = "rate:limit:user:";

  /**
   * 接口限流前缀
   */
  String RATE_LIMIT_API_PREFIX = "rate:limit:api:";

  /**
   * 幂等键前缀
   */
  String IDEMPOTENCY_PREFIX = "idempotency:";

  /**
   * 分布式锁前缀
   */
  String LOCK_PREFIX = "lock:";

  /**
   * 用户在线状态前缀
   */
  String USER_ONLINE_PREFIX = "user:online:";

  /**
   * 设备在线状态前缀
   */
  String DEVICE_ONLINE_PREFIX = "device:online:";

  /**
   * 充电会话状态前缀
   */
  String CHARGING_SESSION_PREFIX = "charging:session:";

  /**
   * 热点数据前缀
   */
  String HOT_DATA_PREFIX = "hot:data:";
}
