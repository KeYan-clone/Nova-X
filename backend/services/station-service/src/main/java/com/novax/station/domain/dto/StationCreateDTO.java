package com.novax.station.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建充电站DTO
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@Schema(description = "创建充电站请求")
public class StationCreateDTO {

    @Schema(description = "站点名称", example = "中关村充电站")
    @NotBlank(message = "站点名称不能为空")
    @Size(max = 100, message = "站点名称长度不能超过100")
    private String stationName;

    @Schema(description = "运营商ID", example = "1001")
    @NotNull(message = "运营商ID不能为空")
    private Long operatorId;

    @Schema(description = "运营商名称", example = "国网电动")
    @NotBlank(message = "运营商名称不能为空")
    private String operatorName;

    @Schema(description = "站点类型", example = "PUBLIC", allowableValues = {"PUBLIC", "PRIVATE", "BUS", "TAXI"})
    @NotBlank(message = "站点类型不能为空")
    @Pattern(regexp = "PUBLIC|PRIVATE|BUS|TAXI", message = "站点类型必须是PUBLIC、PRIVATE、BUS或TAXI")
    private String stationType;

    @Schema(description = "省份", example = "北京市")
    @NotBlank(message = "省份不能为空")
    private String province;

    @Schema(description = "城市", example = "北京市")
    @NotBlank(message = "城市不能为空")
    private String city;

    @Schema(description = "区县", example = "海淀区")
    @NotBlank(message = "区县不能为空")
    private String district;

    @Schema(description = "详细地址", example = "中关村大街1号")
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200")
    private String address;

    @Schema(description = "经度", example = "116.404")
    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180", message = "经度必须在-180到180之间")
    @DecimalMax(value = "180", message = "经度必须在-180到180之间")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "39.915")
    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90", message = "纬度必须在-90到90之间")
    @DecimalMax(value = "90", message = "纬度必须在-90到90之间")
    private BigDecimal latitude;

    @Schema(description = "联系人", example = "张三")
    @NotBlank(message = "联系人不能为空")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13800138000")
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    @Schema(description = "营业开始时间", example = "08:00")
    private String businessStartTime;

    @Schema(description = "营业结束时间", example = "22:00")
    private String businessEndTime;

    @Schema(description = "是否24小时营业", example = "false")
    private Boolean is24Hours = false;

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
