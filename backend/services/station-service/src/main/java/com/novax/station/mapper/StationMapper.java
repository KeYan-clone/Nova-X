package com.novax.station.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.station.domain.entity.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充电站Mapper
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Mapper
public interface StationMapper extends BaseMapper<Station> {

    /**
     * 查询附近的充电站（基于经纬度）
     * 使用Haversine公式计算距离
     *
     * @param longitude 经度
     * @param latitude 纬度
     * @param radius 半径（公里）
     * @param stationType 站点类型
     * @param onlyAvailable 是否只显示可用站点
     * @return 充电站列表
     */
    List<Station> findNearbyStations(@Param("longitude") BigDecimal longitude,
                                     @Param("latitude") BigDecimal latitude,
                                     @Param("radius") Integer radius,
                                     @Param("stationType") String stationType,
                                     @Param("onlyAvailable") Boolean onlyAvailable);
}
