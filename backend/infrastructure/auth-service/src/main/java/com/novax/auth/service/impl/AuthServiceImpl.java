package com.novax.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.DesensitizedUtil;
import com.novax.auth.domain.dto.PasswordLoginDTO;
import com.novax.auth.domain.dto.SmsLoginDTO;
import com.novax.auth.domain.entity.LoginLog;
import com.novax.auth.domain.vo.CaptchaVO;
import com.novax.auth.domain.vo.LoginVO;
import com.novax.auth.mapper.LoginLogMapper;
import com.novax.auth.service.AuthService;
import com.novax.common.core.constant.RedisKeyConstants;
import com.novax.common.core.exception.BusinessException;
import com.novax.common.core.result.ResultCode;
import com.novax.common.redis.util.RedisUtil;
import com.novax.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final LoginLogMapper loginLogMapper;

    /**
     * 验证码过期时间（秒）
     */
    private static final long CAPTCHA_EXPIRE_TIME = 300;

    /**
     * 短信验证码过期时间（秒）
     */
    private static final long SMS_CODE_EXPIRE_TIME = 300;

    /**
     * 访问令牌过期时间（秒）
     */
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 7200;

    /**
     * 刷新令牌过期时间（秒）
     */
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 604800;

    @Override
    public LoginVO passwordLogin(PasswordLoginDTO dto) {
        log.info("用户 {} 尝试密码登录", dto.getUsername());

        // 1. 验证图形验证码
        if (dto.getCaptchaKey() != null && !dto.getCaptchaKey().isEmpty()) {
            validateCaptcha(dto.getCaptchaKey(), dto.getCaptcha());
        }

        // 2. 查询用户信息（这里模拟，实际应该调用account-service）
        // 实际场景应该通过Feign调用用户服务
        Map<String, Object> userInfo = getUserByUsername(dto.getUsername());
        if (userInfo == null) {
            saveLoginLog(null, dto.getUsername(), "password", "failed", "用户不存在");
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 3. 验证密码
        String storedPassword = (String) userInfo.get("password");
        if (!passwordEncoder.matches(dto.getPassword(), storedPassword)) {
            saveLoginLog((Long) userInfo.get("userId"), dto.getUsername(), "password", "failed", "密码错误");
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        // 4. 检查用户状态
        Integer status = (Integer) userInfo.get("status");
        if (status != 1) {
            saveLoginLog((Long) userInfo.get("userId"), dto.getUsername(), "password", "failed", "账户已禁用");
            throw new BusinessException(ResultCode.USER_ACCOUNT_DISABLED);
        }

        // 5. 生成Token
        Long userId = (Long) userInfo.get("userId");
        String username = (String) userInfo.get("username");
        String userType = (String) userInfo.get("userType");

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("userType", userType);

        String accessToken = jwtUtil.generateToken(claims, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.generateToken(claims, REFRESH_TOKEN_EXPIRE_TIME);

        // 6. 存储Token到Redis
        String tokenKey = RedisKeyConstants.USER_TOKEN + userId;
        redisUtil.set(tokenKey, accessToken, ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        String refreshKey = RedisKeyConstants.USER_TOKEN + "refresh:" + userId;
        redisUtil.set(refreshKey, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        // 7. 记录登录日志
        saveLoginLog(userId, username, "password", "success", null);

        log.info("用户 {} 登录成功", username);

        // 8. 返回登录信息
        return LoginVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(ACCESS_TOKEN_EXPIRE_TIME)
                .userId(userId)
                .username(username)
                .realName((String) userInfo.get("realName"))
                .phone(DesensitizedUtil.mobilePhone((String) userInfo.get("phone")))
                .userType(userType)
                .build();
    }

    @Override
    public LoginVO smsLogin(SmsLoginDTO dto) {
        log.info("用户 {} 尝试短信登录", dto.getPhone());

        // 1. 验证短信验证码
        String codeKey = RedisKeyConstants.SMS_CODE + dto.getPhone();
        String storedCode = (String) redisUtil.get(codeKey);
        if (storedCode == null || !storedCode.equals(dto.getSmsCode())) {
            throw new BusinessException(ResultCode.VERIFICATION_CODE_ERROR);
        }

        // 2. 删除验证码
        redisUtil.delete(codeKey);

        // 3. 查询用户信息（模拟）
        Map<String, Object> userInfo = getUserByPhone(dto.getPhone());
        if (userInfo == null) {
            saveLoginLog(null, dto.getPhone(), "sms", "failed", "用户不存在");
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 4. 生成Token（同密码登录）
        Long userId = (Long) userInfo.get("userId");
        String username = (String) userInfo.get("username");
        String userType = (String) userInfo.get("userType");

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("userType", userType);

        String accessToken = jwtUtil.generateToken(claims, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.generateToken(claims, REFRESH_TOKEN_EXPIRE_TIME);

        // 5. 存储Token
        String tokenKey = RedisKeyConstants.USER_TOKEN + userId;
        redisUtil.set(tokenKey, accessToken, ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        String refreshKey = RedisKeyConstants.USER_TOKEN + "refresh:" + userId;
        redisUtil.set(refreshKey, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        // 6. 记录登录日志
        saveLoginLog(userId, username, "sms", "success", null);

        log.info("用户 {} 短信登录成功", username);

        return LoginVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(ACCESS_TOKEN_EXPIRE_TIME)
                .userId(userId)
                .username(username)
                .realName((String) userInfo.get("realName"))
                .phone(DesensitizedUtil.mobilePhone(dto.getPhone()))
                .userType(userType)
                .build();
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        // 1. 验证刷新令牌
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 2. 从Token中获取用户信息
        Map<String, Object> claims = jwtUtil.parseToken(refreshToken);
        Long userId = ((Number) claims.get("userId")).longValue();
        String username = (String) claims.get("username");
        String userType = (String) claims.get("userType");

        // 3. 验证刷新令牌是否在Redis中
        String refreshKey = RedisKeyConstants.USER_TOKEN + "refresh:" + userId;
        String storedRefreshToken = (String) redisUtil.get(refreshKey);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 4. 生成新的访问令牌
        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("userId", userId);
        newClaims.put("username", username);
        newClaims.put("userType", userType);

        String newAccessToken = jwtUtil.generateToken(newClaims, ACCESS_TOKEN_EXPIRE_TIME);

        // 5. 更新Redis中的Token
        String tokenKey = RedisKeyConstants.USER_TOKEN + userId;
        redisUtil.set(tokenKey, newAccessToken, ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        log.info("用户 {} 刷新令牌成功", username);

        return LoginVO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(ACCESS_TOKEN_EXPIRE_TIME)
                .userId(userId)
                .username(username)
                .userType(userType)
                .build();
    }

    @Override
    public void logout(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }

        // 1. 解析Token获取用户ID
        Map<String, Object> claims = jwtUtil.parseToken(token);
        Long userId = ((Number) claims.get("userId")).longValue();
        String username = (String) claims.get("username");

        // 2. 删除Redis中的Token
        String tokenKey = RedisKeyConstants.USER_TOKEN + userId;
        String refreshKey = RedisKeyConstants.USER_TOKEN + "refresh:" + userId;
        redisUtil.delete(tokenKey);
        redisUtil.delete(refreshKey);

        log.info("用户 {} 退出登录", username);
    }

    @Override
    public CaptchaVO getCaptcha() {
        // 1. 生成验证码
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        String code = captcha.getCode();
        String imageBase64 = captcha.getImageBase64();

        // 2. 生成验证码Key
        String captchaKey = UUID.randomUUID().toString(true);

        // 3. 存储到Redis
        String key = RedisKeyConstants.CAPTCHA_CODE + captchaKey;
        redisUtil.set(key, code, CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);

        log.info("生成验证码: key={}, code={}", captchaKey, code);

        return CaptchaVO.builder()
                .captchaKey(captchaKey)
                .captchaImage("data:image/png;base64," + imageBase64)
                .expiresIn(CAPTCHA_EXPIRE_TIME)
                .build();
    }

    @Override
    public void sendSmsCode(String phone) {
        // 1. 生成6位验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // 2. 存储到Redis
        String key = RedisKeyConstants.SMS_CODE + phone;
        redisUtil.set(key, code, SMS_CODE_EXPIRE_TIME, TimeUnit.SECONDS);

        // 3. 发送短信（这里模拟，实际应该调用短信服务）
        log.info("发送短信验证码: phone={}, code={}", phone, code);

        // TODO: 调用短信服务发送验证码
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * 验证图形验证码
     */
    private void validateCaptcha(String captchaKey, String captcha) {
        String key = RedisKeyConstants.CAPTCHA_CODE + captchaKey;
        String storedCode = (String) redisUtil.get(key);

        if (storedCode == null || !storedCode.equalsIgnoreCase(captcha)) {
            throw new BusinessException(ResultCode.VERIFICATION_CODE_ERROR);
        }

        // 验证成功后删除验证码
        redisUtil.delete(key);
    }

    /**
     * 根据用户名查询用户（模拟）
     * 实际应该通过Feign调用account-service
     */
    private Map<String, Object> getUserByUsername(String username) {
        // 模拟数据
        if ("admin".equals(username)) {
            Map<String, Object> user = new HashMap<>();
            user.put("userId", 1L);
            user.put("username", "admin");
            user.put("password", passwordEncoder.encode("123456"));
            user.put("realName", "管理员");
            user.put("phone", "13800138000");
            user.put("userType", "ADMIN");
            user.put("status", 1);
            return user;
        }
        return null;
    }

    /**
     * 根据手机号查询用户（模拟）
     */
    private Map<String, Object> getUserByPhone(String phone) {
        // 模拟数据
        if ("13800138000".equals(phone)) {
            Map<String, Object> user = new HashMap<>();
            user.put("userId", 1L);
            user.put("username", "admin");
            user.put("realName", "管理员");
            user.put("phone", phone);
            user.put("userType", "ADMIN");
            user.put("status", 1);
            return user;
        }
        return null;
    }

    /**
     * 保存登录日志
     */
    private void saveLoginLog(Long userId, String username, String loginType,
                             String status, String failureReason) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUsername(username);
        loginLog.setLoginType(loginType);
        loginLog.setLoginStatus(status);
        loginLog.setFailureReason(failureReason);
        loginLog.setLoginTime(LocalDateTime.now());
        // TODO: 获取真实IP、浏览器、OS信息
        loginLog.setLoginIp("127.0.0.1");
        loginLog.setLoginLocation("本地");
        loginLog.setBrowser("Chrome");
        loginLog.setOs("Windows");

        loginLogMapper.insert(loginLog);
    }
}
