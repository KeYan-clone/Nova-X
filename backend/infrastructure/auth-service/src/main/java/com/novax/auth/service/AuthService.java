package com.novax.auth.service;

import com.novax.auth.domain.dto.PasswordLoginDTO;
import com.novax.auth.domain.dto.SmsLoginDTO;
import com.novax.auth.domain.vo.CaptchaVO;
import com.novax.auth.domain.vo.LoginVO;

/**
 * 认证服务接口
 *
 * @author Nova-X
 * @since 2026-01-20
 */
public interface AuthService {

    /**
     * 密码登录
     *
     * @param dto 登录信息
     * @return 登录结果
     */
    LoginVO passwordLogin(PasswordLoginDTO dto);

    /**
     * 短信登录
     *
     * @param dto 登录信息
     * @return 登录结果
     */
    LoginVO smsLogin(SmsLoginDTO dto);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 退出登录
     *
     * @param token 访问令牌
     */
    void logout(String token);

    /**
     * 获取图形验证码
     *
     * @return 验证码信息
     */
    CaptchaVO getCaptcha();

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     */
    void sendSmsCode(String phone);

    /**
     * 验证Token
     *
     * @param token 访问令牌
     * @return 是否有效
     */
    boolean validateToken(String token);
}
