package com.novax.account.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息 VO
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号（脱敏）
     */
    private String phone;

    /**
     * 邮箱（脱敏）
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 实名认证状态（0-未认证，1-已认证）
     */
    private Integer verified;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户类型（1-普通用户，2-运营商，3-OEM，4-电力供应商，5-管理员）
     */
    private Integer userType;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
