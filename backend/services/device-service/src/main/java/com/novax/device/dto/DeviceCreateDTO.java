package com.novax.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备创建DTO
 */
@Data
@Schema(description = "设备创建请求")
public class DeviceCreateDTO {

    @Schema(description = "站点ID", required = true)
    @NotNull(message = "站点ID不能为空")
    private Long stationId;

    @Schema(description = "设备类型：AC-交流，DC-直流", required = true)
    @NotBlank(message = "设备类型不能为空")
    @Pattern(regexp = "AC|DC", message = "设备类型只能是AC或DC")
    private String deviceType;

    @Schema(description = "制造商", required = true)
    @NotBlank(message = "制造商不能为空")
    @Size(max = 50, message = "制造商名称不能超过50个字符")
    private String manufacturer;

    @Schema(description = "设备型号", required = true)
    @NotBlank(message = "设备型号不能为空")
    @Size(max = 50, message = "设备型号不能超过50个字符")
    private String model;

    @Schema(description = "额定功率(kW)", required = true)
    @NotNull(message = "额定功率不能为空")
    @DecimalMin(value = "0.1", message = "额定功率必须大于0")
    private BigDecimal ratedPower;

    @Schema(description = "额定电压(V)", required = true)
    @NotNull(message = "额定电压不能为空")
    @DecimalMin(value = "1", message = "额定电压必须大于0")
    private BigDecimal ratedVoltage;

    @Schema(description = "额定电流(A)", required = true)
    @NotNull(message = "额定电流不能为空")
    @DecimalMin(value = "0.1", message = "额定电流必须大于0")
    private BigDecimal ratedCurrent;

    @Schema(description = "充电枪数量", required = true)
    @NotNull(message = "充电枪数量不能为空")
    @Min(value = 1, message = "充电枪数量至少为1")
    @Max(value = 10, message = "充电枪数量最多为10")
    private Integer connectorCount;

    @Schema(description = "IP地址")
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
             message = "IP地址格式不正确")
    private String ipAddress;

    @Schema(description = "固件版本")
    @Size(max = 20, message = "固件版本不能超过20个字符")
    private String firmwareVersion;

    @Schema(description = "安装日期")
    private LocalDateTime installDate;

    @Schema(description = "质保截止日期")
    private LocalDateTime warrantyExpireDate;

    @Schema(description = "维护周期(天)")
    @Min(value = 1, message = "维护周期至少为1天")
    private Integer maintenanceCycle;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;
}
