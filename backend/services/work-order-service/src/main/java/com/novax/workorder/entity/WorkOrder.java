package com.novax.workorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("work_order")
@Schema(description = "工单")
public class WorkOrder extends BaseEntity {

    @Schema(description = "工单编号")
    private String orderNo;

    @Schema(description = "工单类型: FAULT-故障, MAINTENANCE-维护, INSPECTION-巡检, INSTALL-安装")
    private String orderType;

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "故障描述")
    private String faultDescription;

    @Schema(description = "优先级: LOW-低, MEDIUM-中, HIGH-高, URGENT-紧急")
    private String priority;

    @Schema(description = "工单状态: PENDING-待处理, ASSIGNED-已分配, IN_PROGRESS-处理中, COMPLETED-已完成, CANCELLED-已取消")
    private String orderStatus;

    @Schema(description = "报修人ID")
    private Long reporterId;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "处理人姓名")
    private String handlerName;

    @Schema(description = "分配时间")
    private LocalDateTime assignTime;

    @Schema(description = "开始处理时间")
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "处理结果")
    private String handleResult;

    @Schema(description = "备注")
    private String remark;
}
