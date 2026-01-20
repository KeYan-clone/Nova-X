package com.novax.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备更新DTO
 */
@Data
@Schema(description = "设备更新请求")
public class DeviceUpdateDTO {

    @Schema(description = "设备状态：IDLE-空闲，CHARGING-充电中，FAULT-故障，OFFLINE-离线，MAINTENANCE-维护中")
    @Pattern(regexp = "IDLE|CHARGING|FAULT|OFFLINE|MAINTENANCE",
             message = "设备状态不正确")
    private String deviceStatus;

    @Schema(description = "IP地址")
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
             message = "IP地址格式不正确")
    private String ipAddress;

    @Schema(description = "固件版本")
    @Size(max = 20, message = "固件版本不能超过20个字符")
    private String firmwareVersion;

    @Schema(description = "质保截止日期")
    private LocalDateTime warrantyExpireDate;

    @Schema(description = "维护周期(天)")
    @Min(value = 1, message = "维护周期至少为1天")
    private Integer maintenanceCycle;

    @Schema(description = "上次维护时间")
    private LocalDateTime lastMaintenanceTime;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;
}
