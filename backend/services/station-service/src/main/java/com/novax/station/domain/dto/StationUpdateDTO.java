package com.novax.station.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新充电站DTO
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@Schema(description = "更新充电站请求")
public class StationUpdateDTO {

    @Schema(description = "站点名称", example = "中关村充电站")
    @Size(max = 100, message = "站点名称长度不能超过100")
    private String stationName;

    @Schema(description = "站点状态", example = "NORMAL", allowableValues = {"NORMAL", "MAINTENANCE", "OFFLINE"})
    @Pattern(regexp = "NORMAL|MAINTENANCE|OFFLINE", message = "站点状态必须是NORMAL、MAINTENANCE或OFFLINE")
    private String stationStatus;

    @Schema(description = "详细地址", example = "中关村大街1号")
    @Size(max = 200, message = "详细地址长度不能超过200")
    private String address;

    @Schema(description = "经度", example = "116.404")
    @DecimalMin(value = "-180", message = "经度必须在-180到180之间")
    @DecimalMax(value = "180", message = "经度必须在-180到180之间")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "39.915")
    @DecimalMin(value = "-90", message = "纬度必须在-90到90之间")
    @DecimalMax(value = "90", message = "纬度必须在-90到90之间")
    private BigDecimal latitude;

    @Schema(description = "联系人", example = "张三")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    @Schema(description = "营业开始时间", example = "08:00")
    private String businessStartTime;

    @Schema(description = "营业结束时间", example = "22:00")
    private String businessEndTime;

    @Schema(description = "是否24小时营业", example = "false")
    private Boolean is24Hours;

    @Schema(description = "停车费说明", example = "前2小时免费")
    private String parkingFee;

    @Schema(description = "站点图片（多张，逗号分隔）")
    private String stationImages;

    @Schema(description = "站点描述")
    @Size(max = 500, message = "站点描述长度不能超过500")
    private String description;

    @Schema(description = "设施说明", example = "有卫生间、休息室、便利店")
    @Size(max = 200, message = "设施说明长度不能超过200")
    private String facilities;
}
