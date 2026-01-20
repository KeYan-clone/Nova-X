package com.novax.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备视图对象
 */
@Data
@Schema(description = "设备信息")
public class DeviceVO {

    @Schema(description = "设备ID")
    private Long id;

    @Schema(description = "设备编码")
    private String deviceCode;

    @Schema(description = "站点ID")
    private Long stationId;

    @Schema(description = "站点名称")
    private String stationName;

    @Schema(description = "设备类型：AC-交流，DC-直流")
    private String deviceType;

    @Schema(description = "设备状态：IDLE-空闲，CHARGING-充电中，FAULT-故障，OFFLINE-离线，MAINTENANCE-维护中")
    private String deviceStatus;

    @Schema(description = "设备状态显示")
    private String deviceStatusDisplay;

    @Schema(description = "制造商")
    private String manufacturer;

    @Schema(description = "设备型号")
    private String model;

    @Schema(description = "额定功率(kW)")
    private BigDecimal ratedPower;

    @Schema(description = "额定电压(V)")
    private BigDecimal ratedVoltage;

    @Schema(description = "额定电流(A)")
    private BigDecimal ratedCurrent;

    @Schema(description = "充电枪数量")
    private Integer connectorCount;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "固件版本")
    private String firmwareVersion;

    @Schema(description = "最后在线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineTime;

    @Schema(description = "最后离线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOfflineTime;

    @Schema(description = "累计充电次数")
    private Integer totalChargingCount;

    @Schema(description = "累计充电电量(kWh)")
    private BigDecimal totalChargingEnergy;

    @Schema(description = "累计工作时长(小时)")
    private BigDecimal totalWorkingHours;

    @Schema(description = "安装日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime installDate;

    @Schema(description = "质保截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime warrantyExpireDate;

    @Schema(description = "维护周期(天)")
    private Integer maintenanceCycle;

    @Schema(description = "上次维护时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMaintenanceTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "充电枪列表")
    private List<ConnectorVO> connectors;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
