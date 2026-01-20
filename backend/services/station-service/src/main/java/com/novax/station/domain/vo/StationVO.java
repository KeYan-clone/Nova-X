package com.novax.station.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电站VO
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@Schema(description = "充电站信息")
public class StationVO {

    @Schema(description = "站点ID", example = "1001")
    private Long id;

    @Schema(description = "站点编号", example = "ST202601200001")
    private String stationCode;

    @Schema(description = "站点名称", example = "中关村充电站")
    private String stationName;

    @Schema(description = "运营商ID", example = "1")
    private Long operatorId;

    @Schema(description = "运营商名称", example = "国网电动")
    private String operatorName;

    @Schema(description = "站点类型", example = "PUBLIC")
    private String stationType;

    @Schema(description = "站点状态", example = "NORMAL")
    private String stationStatus;

    @Schema(description = "省份", example = "北京市")
    private String province;

    @Schema(description = "城市", example = "北京市")
    private String city;

    @Schema(description = "区县", example = "海淀区")
    private String district;

    @Schema(description = "详细地址", example = "中关村大街1号")
    private String address;

    @Schema(description = "经度", example = "116.404")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "39.915")
    private BigDecimal latitude;

    @Schema(description = "联系人", example = "张三")
    private String contactPerson;

    @Schema(description = "联系电话", example = "138****8000")
    private String contactPhone;

    @Schema(description = "总桩数", example = "20")
    private Integer totalPiles;

    @Schema(description = "可用桩数", example = "15")
    private Integer availablePiles;

    @Schema(description = "总枪数", example = "40")
    private Integer totalConnectors;

    @Schema(description = "可用枪数", example = "30")
    private Integer availableConnectors;

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
    private String description;

    @Schema(description = "设施说明", example = "有卫生间、休息室、便利店")
    private String facilities;

    @Schema(description = "评分", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "评价数量", example = "120")
    private Integer reviewCount;

    @Schema(description = "距离（公里）", example = "2.5")
    private BigDecimal distance;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
