package com.novax.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novax.device.dto.ConnectorCreateDTO;
import com.novax.device.dto.DeviceCreateDTO;
import com.novax.device.dto.DeviceUpdateDTO;
import com.novax.device.vo.ConnectorVO;
import com.novax.device.vo.DeviceVO;

import java.util.List;

/**
 * 设备服务接口
 */
public interface DeviceService {

    /**
     * 创建设备
     */
    Long createDevice(DeviceCreateDTO dto);

    /**
     * 更新设备
     */
    void updateDevice(Long id, DeviceUpdateDTO dto);

    /**
     * 删除设备
     */
    void deleteDevice(Long id);

    /**
     * 根据ID查询设备
     */
    DeviceVO getDeviceById(Long id);

    /**
     * 分页查询设备
     */
    Page<DeviceVO> pageDevices(int pageNum, int pageSize, Long stationId, String deviceStatus);

    /**
     * 根据站点ID查询设备列表
     */
    List<DeviceVO> getDevicesByStationId(Long stationId);

    /**
     * 创建充电枪
     */
    Long createConnector(ConnectorCreateDTO dto);

    /**
     * 根据设备ID查询充电枪列表
     */
    List<ConnectorVO> getConnectorsByDeviceId(Long deviceId);

    /**
     * 更新设备状态
     */
    void updateDeviceStatus(Long id, String deviceStatus);

    /**
     * 更新充电枪状态
     */
    void updateConnectorStatus(Long connectorId, String connectorStatus);

    /**
     * 设备上线
     */
    void deviceOnline(Long id);

    /**
     * 设备离线
     */
    void deviceOffline(Long id);
}
