package com.novax.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "充电统计VO")
public class ChargingStatisticsVO {

    @Schema(description = "总充电次数")
    private Long totalChargingCount;

    @Schema(description = "总充电电量(kWh)")
    private BigDecimal totalEnergy;

    @Schema(description = "总充电时长(分钟)")
    private Long totalDuration;

    @Schema(description = "总充电金额")
    private BigDecimal totalAmount;

    @Schema(description = "平均单次充电电量")
    private BigDecimal avgEnergy;

    @Schema(description = "平均单次充电时长")
    private Long avgDuration;

    @Schema(description = "设备使用率(%)")
    private BigDecimal deviceUtilization;

    @Schema(description = "统计日期")
    private String statisticsDate;
}
