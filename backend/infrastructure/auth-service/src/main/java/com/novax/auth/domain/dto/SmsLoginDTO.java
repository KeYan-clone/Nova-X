package com.novax.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 短信登录DTO
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@Schema(description = "短信登录请求")
public class SmsLoginDTO {

    @Schema(description = "手机号", example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "短信验证码", example = "123456")
    @NotBlank(message = "验证码不能为空")
    private String smsCode;

    @Schema(description = "是否记住我", example = "false")
    private Boolean rememberMe = false;
}
