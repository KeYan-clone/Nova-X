package com.novax.drvpp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("demand_response_event")
@Schema(description = "需求响应事件")
public class DemandResponseEvent extends BaseEntity {

    @Schema(description = "事件编号")
    private String eventNo;

    @Schema(description = "事件名称")
    private String eventName;

    @Schema(description = "事件类型: PEAK_SHAVING-削峰, VALLEY_FILLING-填谷, EMERGENCY-应急")
    private String eventType;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "目标削减功率(kW)")
    private BigDecimal targetPowerReduction;

    @Schema(description = "实际削减功率(kW)")
    private BigDecimal actualPowerReduction;

    @Schema(description = "激励单价(元/kW)")
    private BigDecimal incentivePrice;

    @Schema(description = "总激励金额(元)")
    private BigDecimal totalIncentive;

    @Schema(description = "参与设备数")
    private Integer participantCount;

    @Schema(description = "事件状态: PLANNED-计划中, ACTIVE-进行中, COMPLETED-已完成, CANCELLED-已取消")
    private String eventStatus;

    @Schema(description = "事件描述")
    private String description;
}
