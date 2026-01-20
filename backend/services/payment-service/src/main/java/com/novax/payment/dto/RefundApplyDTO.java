package com.novax.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 退款申请DTO
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@Schema(description = "退款申请DTO")
public class RefundApplyDTO {

    @Schema(description = "支付记录ID", required = true)
    @NotNull(message = "支付记录ID不能为空")
    private Long paymentId;

    @Schema(description = "退款金额", required = true, example = "32.11")
    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    private BigDecimal refundAmount;

    @Schema(description = "退款原因", required = true)
    @NotBlank(message = "退款原因不能为空")
    private String refundReason;
}
