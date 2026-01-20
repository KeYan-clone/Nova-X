package com.novax.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 登录日志实体
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("login_log")
public class LoginLog extends BaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录类型：password-密码登录, sms-短信登录, oauth-第三方登录
     */
    private String loginType;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态：success-成功, failed-失败
     */
    private String loginStatus;

    /**
     * 失败原因
     */
    private String failureReason;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
}
