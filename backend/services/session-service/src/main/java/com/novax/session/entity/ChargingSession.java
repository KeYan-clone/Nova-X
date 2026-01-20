package com.novax.session.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电会话实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging_session")
public class ChargingSession extends BaseEntity {

    /** 会话编号 */
    private String sessionCode;

    /** 用户ID */
    private Long userId;

    /** 站点ID */
    private Long stationId;

    /** 设备ID */
    private Long deviceId;

    /** 充电枪ID */
    private Long connectorId;

    /** 会话状态: PENDING-待开始, CHARGING-充电中, COMPLETED-已完成, FAILED-失败 */
    private String sessionStatus;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 充电电量(kWh) */
    private BigDecimal chargingEnergy;

    /** 开始SOC(%) */
    private Integer startSoc;

    /** 结束SOC(%) */
    private Integer endSoc;

    /** 总费用(元) */
    private BigDecimal totalCost;

    /** 电费(元) */
    private BigDecimal electricityCost;

    /** 服务费(元) */
    private BigDecimal serviceCost;

    /** 停车费(元) */
    private BigDecimal parkingCost;

    /** 停止原因 */
    private String stopReason;

    /** 备注 */
    private String remark;
}
