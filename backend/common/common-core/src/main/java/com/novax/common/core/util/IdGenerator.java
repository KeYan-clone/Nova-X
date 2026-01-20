package com.novax.common.core.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID 生成工具类
 */
public class IdGenerator {

  private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);
  private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

  /**
   * 生成唯一ID（雪花算法）
   */
  public static Long generateId() {
    return SNOWFLAKE.nextId();
  }

  /**
   * 生成唯一ID字符串
   */
  public static String generateIdStr() {
    return String.valueOf(SNOWFLAKE.nextId());
  }

  /**
   * 生成UUID（无连字符）
   */
  public static String generateUuid() {
    return IdUtil.simpleUUID();
  }

  /**
   * 生成UUID（带连字符）
   */
  public static String generateUuidWithDash() {
    return IdUtil.randomUUID();
  }

  /**
   * 生成订单号
   * 格式：O + 时间戳 + 6位随机数
   */
  public static String generateOrderNo() {
    return "O" + System.currentTimeMillis() + getThreadSafeSequence();
  }

  /**
   * 生成充电会话号
   * 格式：S + 时间戳 + 6位随机数
   */
  public static String generateSessionNo() {
    return "S" + System.currentTimeMillis() + getThreadSafeSequence();
  }

  /**
   * 生成工单号
   * 格式：W + 时间戳 + 6位随机数
   */
  public static String generateWorkOrderNo() {
    return "W" + System.currentTimeMillis() + getThreadSafeSequence();
  }

  /**
   * 生成支付流水号
   * 格式：P + 时间戳 + 6位随机数
   */
  public static String generatePaymentNo() {
    return "P" + System.currentTimeMillis() + getThreadSafeSequence();
  }

  /**
   * 获取线程安全的6位序列号
   */
  private static String getThreadSafeSequence() {
    int seq = SEQUENCE.incrementAndGet();
    if (seq >= 999999) {
      SEQUENCE.set(0);
      seq = SEQUENCE.incrementAndGet();
    }
    return String.format("%06d", seq % 1000000);
  }
}
