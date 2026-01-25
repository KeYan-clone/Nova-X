package com.novax.auth.controller;

import com.novax.auth.domain.dto.PasswordLoginDTO;
import com.novax.auth.domain.dto.SmsLoginDTO;
import com.novax.auth.domain.vo.CaptchaVO;
import com.novax.auth.domain.vo.LoginVO;
import com.novax.auth.service.AuthService;
import com.novax.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "认证管理", description = "用户登录、登出、令牌刷新等认证相关接口")
public class AuthController {

    private final AuthService authService;

    /**
     * 密码登录
     */
    @PostMapping("/login/password")
    @Operation(summary = "密码登录", description = "使用用户名/手机号/邮箱+密码登录")
    public Result<LoginVO> passwordLogin(@Valid @RequestBody PasswordLoginDTO dto) {
        LoginVO loginVO = authService.passwordLogin(dto);
        return Result.success(loginVO);
    }

    /**
     * 短信登录
     */
    @PostMapping("/login/sms")
    @Operation(summary = "短信登录", description = "使用手机号+短信验证码登录")
    public Result<LoginVO> smsLogin(@Valid @RequestBody SmsLoginDTO dto) {
        LoginVO loginVO = authService.smsLogin(dto);
        return Result.success(loginVO);
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public Result<LoginVO> refreshToken(
            @Parameter(description = "刷新令牌", required = true) @RequestHeader("Refresh-Token") String refreshToken) {
        LoginVO loginVO = authService.refreshToken(refreshToken);
        return Result.success(loginVO);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "退出当前登录状态，清除令牌")
    public Result<Void> logout(
            @Parameter(description = "访问令牌") @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            authService.logout(token);
        }
        return Result.success();
    }

    /**
     * 获取图形验证码
     */
    @GetMapping("/captcha")
    @Operation(summary = "获取图形验证码", description = "生成图形验证码，用于登录时验证")
    public Result<CaptchaVO> getCaptcha() {
        CaptchaVO captchaVO = authService.getCaptcha();
        return Result.success(captchaVO);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms-code")
    @Operation(summary = "发送短信验证码", description = "发送短信验证码到指定手机号")
    public Result<Void> sendSmsCode(
            @Parameter(description = "手机号", required = true, example = "13800138000") @RequestParam @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone) {
        authService.sendSmsCode(phone);
        return Result.success();
    }

    /**
     * 验证Token
     */
    @GetMapping("/validate")
    @Operation(summary = "验证Token", description = "验证访问令牌是否有效")
    public Result<Boolean> validateToken(
            @Parameter(description = "访问令牌", required = true) @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        boolean valid = authService.validateToken(token);
        return Result.success(valid);
    }
}
