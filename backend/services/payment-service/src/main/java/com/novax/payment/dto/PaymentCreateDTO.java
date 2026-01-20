package com.novax.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 创建支付DTO
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@Schema(description = "创建支付DTO")
public class PaymentCreateDTO {

    @Schema(description = "账单ID", required = true)
    @NotNull(message = "账单ID不能为空")
    private Long billId;

    @Schema(description = "支付金额", required = true, example = "32.11")
    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    private BigDecimal paymentAmount;

    @Schema(description = "支付方式: WECHAT, ALIPAY, UNIONPAY, BALANCE", required = true)
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    @Schema(description = "支付渠道: WECHAT_APP, WECHAT_H5, ALIPAY_APP, etc")
    private String paymentChannel;

    @Schema(description = "用户IP")
    private String clientIp;

    @Schema(description = "设备信息")
    private String deviceInfo;
}
