package com.novax.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 密码登录DTO
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@Schema(description = "密码登录请求")
public class PasswordLoginDTO {

    @Schema(description = "用户名/手机号/邮箱", example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "验证码", example = "1234")
    private String captcha;

    @Schema(description = "验证码Key", example = "uuid-xxxx")
    private String captchaKey;

    @Schema(description = "是否记住我", example = "false")
    private Boolean rememberMe = false;
}
