package com.novax.pricing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.novax.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 定价模板实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pricing_template")
public class PricingTemplate extends BaseEntity {

    /** 模板名称 */
    private String templateName;

    /** 站点ID (null表示通用模板) */
    private Long stationId;

    /** 模板类型: TIME_OF_USE-分时电价, FIXED-固定电价 */
    private String templateType;

    /** 尖峰时段电价(元/kWh) */
    private BigDecimal peakPrice;

    /** 平时段电价(元/kWh) */
    private BigDecimal normalPrice;

    /** 谷时段电价(元/kWh) */
    private BigDecimal valleyPrice;

    /** 服务费(元/kWh) */
    private BigDecimal serviceFee;

    /** 停车费(元/小时) */
    private BigDecimal parkingFee;

    /** 生效日期 */
    private LocalDate effectiveDate;

    /** 失效日期 */
    private LocalDate expiryDate;

    /** 状态: ACTIVE-生效, INACTIVE-失效 */
    private String status;

    /** 备注 */
    private String remark;
}
