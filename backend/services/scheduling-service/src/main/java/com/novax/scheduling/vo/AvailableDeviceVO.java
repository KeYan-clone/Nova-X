package com.novax.scheduling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "可用设备VO")
public class AvailableDeviceVO {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "最大功率(kW)")
    private BigDecimal maxPower;

    @Schema(description = "当前状态")
    private String status;

    @Schema(description = "可用性评分")
    private Integer availabilityScore;
}
