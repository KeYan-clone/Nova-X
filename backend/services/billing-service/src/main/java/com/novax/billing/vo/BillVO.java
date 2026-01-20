package com.novax.billing.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单VO
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Data
@Schema(description = "账单VO")
public class BillVO {

    @Schema(description = "账单ID")
    private Long id;

    @Schema(description = "账单编号")
    private String billNo;

    @Schema(description = "会话ID")
    private Long sessionId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "充电枪编号")
    private String connectorCode;

    @Schema(description = "充电开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "充电结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "充电时长(分钟)")
    private Integer chargingDuration;

    @Schema(description = "充电电量(kWh)")
    private BigDecimal chargingEnergy;

    @Schema(description = "电费(元)")
    private BigDecimal electricityCost;

    @Schema(description = "服务费(元)")
    private BigDecimal serviceFee;

    @Schema(description = "停车费(元)")
    private BigDecimal parkingFee;

    @Schema(description = "优惠金额(元)")
    private BigDecimal discountAmount;

    @Schema(description = "总金额(元)")
    private BigDecimal totalAmount;

    @Schema(description = "实付金额(元)")
    private BigDecimal paidAmount;

    @Schema(description = "账单状态")
    private String billStatus;

    @Schema(description = "账单状态描述")
    private String billStatusDesc;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
