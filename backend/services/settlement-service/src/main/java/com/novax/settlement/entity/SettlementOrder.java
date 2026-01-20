package com.novax.settlement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("settlement_order")
@Schema(description = "结算单")
public class SettlementOrder extends BaseEntity {

    @Schema(description = "结算单号")
    private String settlementNo;

    @Schema(description = "结算周期类型: DAILY-日结, WEEKLY-周结, MONTHLY-月结")
    private String cycleType;

    @Schema(description = "结算开始日期")
    private LocalDate startDate;

    @Schema(description = "结算结束日期")
    private LocalDate endDate;

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "总交易金额")
    private BigDecimal totalAmount;

    @Schema(description = "平台服务费")
    private BigDecimal platformFee;

    @Schema(description = "电费成本")
    private BigDecimal electricityCost;

    @Schema(description = "充电站分成")
    private BigDecimal stationShare;

    @Schema(description = "结算状态: PENDING-待结算, CALCULATED-已计算, CONFIRMED-已确认, PAID-已支付")
    private String settlementStatus;

    @Schema(description = "订单数量")
    private Integer orderCount;

    @Schema(description = "确认时间")
    private LocalDateTime confirmTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;
}
