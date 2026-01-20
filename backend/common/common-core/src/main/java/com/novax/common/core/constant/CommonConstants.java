package com.novax.common.core.constant;

/**
 * 通用常量
 */
public interface CommonConstants {

  /**
   * 成功标识
   */
  Integer SUCCESS = 0;

  /**
   * 失败标识
   */
  Integer FAIL = 1;

  /**
   * 启用状态
   */
  Integer ENABLED = 1;

  /**
   * 禁用状态
   */
  Integer DISABLED = 0;

  /**
   * 删除标识 - 未删除
   */
  Integer NOT_DELETED = 0;

  /**
   * 删除标识 - 已删除
   */
  Integer DELETED = 1;

  /**
   * UTF-8 字符集
   */
  String UTF8 = "UTF-8";

  /**
   * JSON 内容类型
   */
  String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

  /**
   * 默认日期时间格式
   */
  String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /**
   * 默认日期格式
   */
  String DATE_FORMAT = "yyyy-MM-dd";

  /**
   * 默认时间格式
   */
  String TIME_FORMAT = "HH:mm:ss";

  /**
   * ISO 8601 日期时间格式
   */
  String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
}
