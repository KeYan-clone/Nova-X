package com.novax.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging_reservation")
@Schema(description = "充电预约")
public class ChargingReservation extends BaseEntity {

    @Schema(description = "预约编号")
    private String reservationNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "预约开始时间")
    private LocalDateTime reservationStartTime;

    @Schema(description = "预约结束时间")
    private LocalDateTime reservationEndTime;

    @Schema(description = "预计充电电量")
    private BigDecimal estimatedEnergy;

    @Schema(description = "预约状态: PENDING-待确认, CONFIRMED-已确认, IN_USE-使用中, COMPLETED-已完成, CANCELLED-已取消, EXPIRED-已过期")
    private String reservationStatus;

    @Schema(description = "实际使用的会话ID")
    private Long sessionId;

    @Schema(description = "取消原因")
    private String cancelReason;
}
