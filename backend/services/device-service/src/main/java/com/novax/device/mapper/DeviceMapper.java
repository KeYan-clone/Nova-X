package com.novax.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.device.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备Mapper
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 根据站点ID查询设备列表
     */
    List<Device> findByStationId(@Param("stationId") Long stationId);

    /**
     * 根据设备状态查询设备列表
     */
    List<Device> findByStatus(@Param("deviceStatus") String deviceStatus);

    /**
     * 统计站点设备数量
     */
    Integer countByStationId(@Param("stationId") Long stationId);
}
