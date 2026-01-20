package com.novax.payment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付结果VO
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@Schema(description = "支付结果VO")
public class PaymentResultVO {

    @Schema(description = "支付记录ID")
    private Long paymentId;

    @Schema(description = "支付单号")
    private String paymentNo;

    @Schema(description = "账单编号")
    private String billNo;

    @Schema(description = "支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "支付状态描述")
    private String paymentStatusDesc;

    @Schema(description = "第三方支付单号")
    private String thirdPaymentNo;

    @Schema(description = "支付跳转URL（用于H5/小程序支付）")
    private String paymentUrl;

    @Schema(description = "支付二维码（用于扫码支付）")
    private String paymentQrCode;

    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
