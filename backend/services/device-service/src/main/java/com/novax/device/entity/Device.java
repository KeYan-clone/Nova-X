package com.novax.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电桩设备实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("device")
public class Device extends BaseEntity {

    /**
     * 设备编码（唯一标识）
     */
    private String deviceCode;

    /**
     * 站点ID
     */
    private Long stationId;

    /**
     * 设备类型：AC-交流，DC-直流
     */
    private String deviceType;

    /**
     * 设备状态：IDLE-空闲，CHARGING-充电中，FAULT-故障，OFFLINE-离线，MAINTENANCE-维护中
     */
    private String deviceStatus;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 额定功率(kW)
     */
    private BigDecimal ratedPower;

    /**
     * 额定电压(V)
     */
    private BigDecimal ratedVoltage;

    /**
     * 额定电流(A)
     */
    private BigDecimal ratedCurrent;

    /**
     * 充电枪数量
     */
    private Integer connectorCount;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 最后在线时间
     */
    private LocalDateTime lastOnlineTime;

    /**
     * 最后离线时间
     */
    private LocalDateTime lastOfflineTime;

    /**
     * 累计充电次数
     */
    private Integer totalChargingCount;

    /**
     * 累计充电电量(kWh)
     */
    private BigDecimal totalChargingEnergy;

    /**
     * 累计工作时长(小时)
     */
    private BigDecimal totalWorkingHours;

    /**
     * 安装日期
     */
    private LocalDateTime installDate;

    /**
     * 质保截止日期
     */
    private LocalDateTime warrantyExpireDate;

    /**
     * 维护周期(天)
     */
    private Integer maintenanceCycle;

    /**
     * 上次维护时间
     */
    private LocalDateTime lastMaintenanceTime;

    /**
     * 备注
     */
    private String remark;
}
