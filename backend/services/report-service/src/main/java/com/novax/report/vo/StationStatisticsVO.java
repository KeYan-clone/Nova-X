package com.novax.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "充电站统计VO")
public class StationStatisticsVO {

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "充电次数")
    private Long chargingCount;

    @Schema(description = "充电电量")
    private BigDecimal totalEnergy;

    @Schema(description = "收入金额")
    private BigDecimal revenue;

    @Schema(description = "设备数量")
    private Integer deviceCount;

    @Schema(description = "在线设备数")
    private Integer onlineDeviceCount;

    @Schema(description = "故障设备数")
    private Integer faultDeviceCount;
}
