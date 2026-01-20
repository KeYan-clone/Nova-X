package com.novax.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "收入统计VO")
public class RevenueStatisticsVO {

    @Schema(description = "充电收入")
    private BigDecimal chargingRevenue;

    @Schema(description = "服务费收入")
    private BigDecimal serviceFeeRevenue;

    @Schema(description = "停车费收入")
    private BigDecimal parkingRevenue;

    @Schema(description = "总收入")
    private BigDecimal totalRevenue;

    @Schema(description = "会员充值金额")
    private BigDecimal rechargeAmount;

    @Schema(description = "订单数量")
    private Long orderCount;

    @Schema(description = "统计日期")
    private String statisticsDate;
}
