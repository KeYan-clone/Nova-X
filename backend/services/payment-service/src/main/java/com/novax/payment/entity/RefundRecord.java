package com.novax.payment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录实体
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("refund_record")
@Schema(description = "退款记录实体")
public class RefundRecord extends BaseEntity {

    @Schema(description = "退款单号", example = "REF20260120143025001")
    private String refundNo;

    @Schema(description = "支付记录ID")
    private Long paymentId;

    @Schema(description = "支付单号")
    private String paymentNo;

    @Schema(description = "账单ID")
    private Long billId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "退款金额(元)", example = "32.11")
    private BigDecimal refundAmount;

    @Schema(description = "退款原因")
    private String refundReason;

    @Schema(description = "退款状态: PENDING-待退款, PROCESSING-退款中, SUCCESS-退款成功, FAILED-退款失败")
    private String refundStatus;

    @Schema(description = "第三方退款单号")
    private String thirdRefundNo;

    @Schema(description = "退款完成时间")
    private LocalDateTime refundTime;

    @Schema(description = "退款失败原因")
    private String failureReason;

    @Schema(description = "备注")
    private String remark;
}
