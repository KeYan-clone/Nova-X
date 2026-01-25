package com.novax.drvpp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vpp_device")
@Schema(description = "虚拟电厂设备")
public class VppDevice extends BaseEntity {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "充电站ID")
    private Long stationId;

    @Schema(description = "充电站名称")
    private String stationName;

    @Schema(description = "可调节容量(kW)")
    private BigDecimal adjustableCapacity;

    @Schema(description = "当前功率(kW)")
    private BigDecimal currentPower;

    @Schema(description = "是否在线")
    private Boolean isOnline;

    @Schema(description = "是否可参与调度")
    private Boolean isAdjustable;

    @Schema(description = "参与响应次数")
    private Integer responseCount;

    @Schema(description = "累计削减功率(kWh)")
    private BigDecimal totalReduction;

    @Schema(description = "累计收益(元)")
    private BigDecimal totalEarnings;
}
