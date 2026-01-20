package com.novax.billing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单实体
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bill")
@Schema(description = "账单实体")
public class Bill extends BaseEntity {

    @Schema(description = "账单编号", example = "BL20260120143025001")
    private String billNo;

    @Schema(description = "会话ID")
    private Long sessionId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "充电枪ID")
    private Long connectorId;

    @Schema(description = "充电枪编号")
    private String connectorCode;

    @Schema(description = "充电开始时间")
    private LocalDateTime startTime;

    @Schema(description = "充电结束时间")
    private LocalDateTime endTime;

    @Schema(description = "充电时长(分钟)")
    private Integer chargingDuration;

    @Schema(description = "充电电量(kWh)", example = "35.68")
    private BigDecimal chargingEnergy;

    @Schema(description = "定价模板ID")
    private Long pricingTemplateId;

    @Schema(description = "电费(元)", example = "28.54")
    private BigDecimal electricityCost;

    @Schema(description = "服务费(元)", example = "3.57")
    private BigDecimal serviceFee;

    @Schema(description = "停车费(元)", example = "0.00")
    private BigDecimal parkingFee;

    @Schema(description = "优惠金额(元)", example = "0.00")
    private BigDecimal discountAmount;

    @Schema(description = "总金额(元)", example = "32.11")
    private BigDecimal totalAmount;

    @Schema(description = "实付金额(元)", example = "32.11")
    private BigDecimal paidAmount;

    @Schema(description = "账单状态: UNPAID-未支付, PAID-已支付, REFUNDED-已退款, CANCELLED-已取消",
            example = "UNPAID")
    private String billStatus;

    @Schema(description = "支付方式: WECHAT-微信, ALIPAY-支付宝, UNIONPAY-银联, BALANCE-余额")
    private String paymentMethod;

    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    @Schema(description = "支付流水号")
    private String paymentTransactionNo;

    @Schema(description = "备注")
    private String remark;
}
