package com.novax.station.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 充电站实体
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("station")
public class Station extends BaseEntity {

    /**
     * 站点编号（唯一标识）
     */
    private String stationCode;

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 运营商ID
     */
    private Long operatorId;

    /**
     * 运营商名称
     */
    private String operatorName;

    /**
     * 站点类型：PUBLIC-公共站, PRIVATE-专用站, BUS-公交站, TAXI-出租车站
     */
    private String stationType;

    /**
     * 站点状态：NORMAL-正常, MAINTENANCE-维护中, OFFLINE-离线
     */
    private String stationStatus;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 总桩数
     */
    private Integer totalPiles;

    /**
     * 可用桩数
     */
    private Integer availablePiles;

    /**
     * 总枪数
     */
    private Integer totalConnectors;

    /**
     * 可用枪数
     */
    private Integer availableConnectors;

    /**
     * 营业开始时间（HH:mm）
     */
    private String businessStartTime;

    /**
     * 营业结束时间（HH:mm）
     */
    private String businessEndTime;

    /**
     * 是否24小时营业
     */
    private Boolean is24Hours;

    /**
     * 停车费说明
     */
    private String parkingFee;

    /**
     * 站点图片（多张，逗号分隔）
     */
    private String stationImages;

    /**
     * 站点描述
     */
    private String description;

    /**
     * 设施说明（如：有卫生间、休息室等）
     */
    private String facilities;

    /**
     * 评分（0-5分）
     */
    private BigDecimal rating;

    /**
     * 评价数量
     */
    private Integer reviewCount;
}
