package com.novax.account.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户注册 DTO
 */
@Data
public class UserRegisterDTO {

    /**
     * 手机号
     */
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number format")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$",
            message = "Password must be 8-20 characters with uppercase, lowercase, and numbers")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "Verification code cannot be empty")
    private String verifyCode;

    /**
     * 昵称
     */
    private String nickname;
}
