package com.novax.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 余额充值DTO
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@Schema(description = "余额充值DTO")
public class RechargeDTO {

    @Schema(description = "充值金额", required = true, example = "100.00")
    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "0.01", message = "充值金额必须大于0")
    private BigDecimal amount;

    @Schema(description = "支付方式: WECHAT, ALIPAY, UNIONPAY", required = true)
    @NotNull(message = "支付方式不能为空")
    private String paymentMethod;
}
