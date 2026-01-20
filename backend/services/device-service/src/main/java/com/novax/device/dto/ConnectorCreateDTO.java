package com.novax.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充电枪创建DTO
 */
@Data
@Schema(description = "充电枪创建请求")
public class ConnectorCreateDTO {

    @Schema(description = "设备ID", required = true)
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "充电枪序号", required = true)
    @NotNull(message = "充电枪序号不能为空")
    @Min(value = 1, message = "充电枪序号至少为1")
    private Integer connectorNo;

    @Schema(description = "充电枪类型：AC-交流，DC_GBT-国标直流，DC_CHADEMO-CHAdeMO，DC_CCS-CCS", required = true)
    @NotBlank(message = "充电枪类型不能为空")
    @Pattern(regexp = "AC|DC_GBT|DC_CHADEMO|DC_CCS", message = "充电枪类型不正确")
    private String connectorType;

    @Schema(description = "最大功率(kW)", required = true)
    @NotNull(message = "最大功率不能为空")
    @DecimalMin(value = "0.1", message = "最大功率必须大于0")
    private BigDecimal maxPower;

    @Schema(description = "最大电压(V)", required = true)
    @NotNull(message = "最大电压不能为空")
    @DecimalMin(value = "1", message = "最大电压必须大于0")
    private BigDecimal maxVoltage;

    @Schema(description = "最大电流(A)", required = true)
    @NotNull(message = "最大电流不能为空")
    @DecimalMin(value = "0.1", message = "最大电流必须大于0")
    private BigDecimal maxCurrent;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;
}
