package com.novax.pricing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.pricing.entity.PricingTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 定价Mapper
 */
@Mapper
public interface PricingMapper extends BaseMapper<PricingTemplate> {

    /**
     * 查询站点生效的定价模板
     */
    PricingTemplate findActiveTemplateByStationId(@Param("stationId") Long stationId);
}
