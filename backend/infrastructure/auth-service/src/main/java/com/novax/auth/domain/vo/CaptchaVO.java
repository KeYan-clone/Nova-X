package com.novax.auth.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码响应VO
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "验证码响应")
public class CaptchaVO {

    @Schema(description = "验证码Key", example = "uuid-xxxx-xxxx")
    private String captchaKey;

    @Schema(description = "验证码图片Base64", example = "data:image/png;base64,iVBORw0KG...")
    private String captchaImage;

    @Schema(description = "过期时间(秒)", example = "300")
    private Long expiresIn = 300L;
}
