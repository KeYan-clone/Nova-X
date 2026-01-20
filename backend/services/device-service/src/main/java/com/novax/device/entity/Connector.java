package com.novax.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 充电枪实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("connector")
public class Connector extends BaseEntity {

    /**
     * 充电枪编码
     */
    private String connectorCode;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 充电枪序号（1号枪、2号枪）
     */
    private Integer connectorNo;

    /**
     * 充电枪类型：AC-交流，DC_GBT-国标直流，DC_CHADEMO-CHAdeMO，DC_CCS-CCS
     */
    private String connectorType;

    /**
     * 充电枪状态：IDLE-空闲，CHARGING-充电中，FAULT-故障，RESERVED-预约中，UNAVAILABLE-不可用
     */
    private String connectorStatus;

    /**
     * 最大功率(kW)
     */
    private BigDecimal maxPower;

    /**
     * 最大电压(V)
     */
    private BigDecimal maxVoltage;

    /**
     * 最大电流(A)
     */
    private BigDecimal maxCurrent;

    /**
     * 当前会话ID
     */
    private Long currentSessionId;

    /**
     * 累计充电次数
     */
    private Integer totalChargingCount;

    /**
     * 累计充电电量(kWh)
     */
    private BigDecimal totalChargingEnergy;

    /**
     * 备注
     */
    private String remark;
}
