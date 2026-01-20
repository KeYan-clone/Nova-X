package com.novax.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.device.entity.Connector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 充电枪Mapper
 */
@Mapper
public interface ConnectorMapper extends BaseMapper<Connector> {

    /**
     * 根据设备ID查询充电枪列表
     */
    List<Connector> findByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 根据状态查询充电枪列表
     */
    List<Connector> findByStatus(@Param("connectorStatus") String connectorStatus);
}
