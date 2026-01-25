package com.novax.payment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment_record")
@Schema(description = "支付记录实体")
public class PaymentRecord extends BaseEntity {

    @Schema(description = "支付单号", example = "PAY20260120143025001")
    private String paymentNo;

    @Schema(description = "账单ID")
    private Long billId;

    @Schema(description = "账单编号")
    private String billNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "支付金额(元)", example = "32.11")
    private BigDecimal paymentAmount;

    @Schema(description = "支付方式: WECHAT-微信, ALIPAY-支付宝, UNIONPAY-银联, BALANCE-余额")
    private String paymentMethod;

    @Schema(description = "支付渠道: WECHAT_APP-微信APP, WECHAT_H5-微信H5, ALIPAY_APP-支付宝APP, etc")
    private String paymentChannel;

    @Schema(description = "支付状态: PENDING-待支付, PAYING-支付中, SUCCESS-支付成功, FAILED-支付失败, CLOSED-已关闭")
    private String paymentStatus;

    @Schema(description = "第三方支付单号")
    private String thirdPaymentNo;

    @Schema(description = "第三方交易流水号")
    private String thirdTransactionNo;

    @Schema(description = "支付完成时间")
    private LocalDateTime paymentTime;

    @Schema(description = "支付失败原因")
    private String failureReason;

    @Schema(description = "回调通知时间")
    private LocalDateTime notifyTime;

    @Schema(description = "回调通知次数")
    private Integer notifyCount;

    @Schema(description = "用户支付IP")
    private String clientIp;

    @Schema(description = "用户设备信息")
    private String deviceInfo;

    @Schema(description = "备注")
    private String remark;
}
