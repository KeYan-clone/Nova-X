package com.novax.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Schema(description = "设备数据DTO")
public class DeviceDataDTO {

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "数据类型: STATUS-状态, TELEMETRY-遥测, ALARM-告警")
    private String dataType;

    @Schema(description = "电压(V)")
    private BigDecimal voltage;

    @Schema(description = "电流(A)")
    private BigDecimal current;

    @Schema(description = "功率(kW)")
    private BigDecimal power;

    @Schema(description = "温度(℃)")
    private BigDecimal temperature;

    @Schema(description = "设备状态")
    private String status;

    @Schema(description = "扩展数据")
    private Map<String, Object> extData;

    @Schema(description = "数据时间")
    private LocalDateTime dataTime;
}
