package com.novax.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充电枪视图对象
 */
@Data
@Schema(description = "充电枪信息")
public class ConnectorVO {

    @Schema(description = "充电枪ID")
    private Long id;

    @Schema(description = "充电枪编码")
    private String connectorCode;

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "充电枪序号")
    private Integer connectorNo;

    @Schema(description = "充电枪类型")
    private String connectorType;

    @Schema(description = "充电枪状态")
    private String connectorStatus;

    @Schema(description = "充电枪状态显示")
    private String connectorStatusDisplay;

    @Schema(description = "最大功率(kW)")
    private BigDecimal maxPower;

    @Schema(description = "最大电压(V)")
    private BigDecimal maxVoltage;

    @Schema(description = "最大电流(A)")
    private BigDecimal maxCurrent;

    @Schema(description = "当前会话ID")
    private Long currentSessionId;

    @Schema(description = "累计充电次数")
    private Integer totalChargingCount;

    @Schema(description = "累计充电电量(kWh)")
    private BigDecimal totalChargingEnergy;

    @Schema(description = "备注")
    private String remark;
}
