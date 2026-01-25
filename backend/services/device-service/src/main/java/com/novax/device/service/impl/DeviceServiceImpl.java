package com.novax.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novax.common.core.exception.BusinessException;
import com.novax.common.core.result.ResultCode;
import com.novax.common.core.util.IdGenerator;
import com.novax.device.dto.ConnectorCreateDTO;
import com.novax.device.dto.DeviceCreateDTO;
import com.novax.device.dto.DeviceUpdateDTO;
import com.novax.device.entity.Connector;
import com.novax.device.entity.Device;
import com.novax.device.mapper.ConnectorMapper;
import com.novax.device.mapper.DeviceMapper;
import com.novax.device.service.DeviceService;
import com.novax.device.vo.ConnectorVO;
import com.novax.device.vo.DeviceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备服务实现
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;
    private final ConnectorMapper connectorMapper;

    private static final Map<String, String> DEVICE_STATUS_MAP = new HashMap<>();
    private static final Map<String, String> CONNECTOR_STATUS_MAP = new HashMap<>();

    static {
        DEVICE_STATUS_MAP.put("IDLE", "空闲");
        DEVICE_STATUS_MAP.put("CHARGING", "充电中");
        DEVICE_STATUS_MAP.put("FAULT", "故障");
        DEVICE_STATUS_MAP.put("OFFLINE", "离线");
        DEVICE_STATUS_MAP.put("MAINTENANCE", "维护中");

        CONNECTOR_STATUS_MAP.put("IDLE", "空闲");
        CONNECTOR_STATUS_MAP.put("CHARGING", "充电中");
        CONNECTOR_STATUS_MAP.put("FAULT", "故障");
        CONNECTOR_STATUS_MAP.put("RESERVED", "预约中");
        CONNECTOR_STATUS_MAP.put("UNAVAILABLE", "不可用");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDevice(DeviceCreateDTO dto) {
        Device device = new Device();
        BeanUtils.copyProperties(dto, device);

        // 生成设备编码: DV + YYYYMMDD + 4位随机数
        String deviceCode = generateDeviceCode();
        device.setDeviceCode(deviceCode);
        device.setDeviceStatus("OFFLINE"); // 初始状态为离线
        device.setTotalChargingCount(0);
        device.setTotalChargingEnergy(java.math.BigDecimal.ZERO);
        device.setTotalWorkingHours(java.math.BigDecimal.ZERO);

        deviceMapper.insert(device);
        return device.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDevice(Long id, DeviceUpdateDTO dto) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND, "设备不存在");
        }

        BeanUtils.copyProperties(dto, device);
        deviceMapper.updateById(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Long id) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND, "设备不存在");
        }

        // 检查是否有充电枪
        List<Connector> connectors = connectorMapper.findByDeviceId(id);
        if (!connectors.isEmpty()) {
            throw new BusinessException(ResultCode.CONFLICT, "该设备下还有充电枪，无法删除");
        }

        deviceMapper.deleteById(id);
    }

    @Override
    public DeviceVO getDeviceById(Long id) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND, "设备不存在");
        }

        DeviceVO vo = convertToVO(device);

        // 查询充电枪列表
        List<ConnectorVO> connectors = getConnectorsByDeviceId(id);
        vo.setConnectors(connectors);

        return vo;
    }

    @Override
    public Page<DeviceVO> pageDevices(int pageNum, int pageSize, Long stationId, String deviceStatus) {
        Page<Device> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        if (stationId != null) {
            wrapper.eq(Device::getStationId, stationId);
        }
        if (deviceStatus != null && !deviceStatus.isEmpty()) {
            wrapper.eq(Device::getDeviceStatus, deviceStatus);
        }
        wrapper.orderByDesc(Device::getCreateTime);

        Page<Device> devicePage = deviceMapper.selectPage(page, wrapper);

        Page<DeviceVO> voPage = new Page<>(devicePage.getCurrent(), devicePage.getSize(), devicePage.getTotal());
        List<DeviceVO> voList = devicePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public List<DeviceVO> getDevicesByStationId(Long stationId) {
        List<Device> devices = deviceMapper.findByStationId(stationId);
        return devices.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createConnector(ConnectorCreateDTO dto) {
        // 验证设备是否存在
        Device device = deviceMapper.selectById(dto.getDeviceId());
        if (device == null) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND, "设备不存在");
        }

        // 检查充电枪序号是否重复
        LambdaQueryWrapper<Connector> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Connector::getDeviceId, dto.getDeviceId())
                .eq(Connector::getConnectorNo, dto.getConnectorNo());
        Long count = connectorMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.RESOURCE_ALREADY_EXISTS, "该设备下已存在" + dto.getConnectorNo() + "号充电枪");
        }

        Connector connector = new Connector();
        BeanUtils.copyProperties(dto, connector);

        // 生成充电枪编码: CN + 设备编码 + 枪号
        String connectorCode = "CN" + device.getDeviceCode() + String.format("%02d", dto.getConnectorNo());
        connector.setConnectorCode(connectorCode);
        connector.setConnectorStatus("IDLE"); // 初始状态为空闲
        connector.setTotalChargingCount(0);
        connector.setTotalChargingEnergy(java.math.BigDecimal.ZERO);

        connectorMapper.insert(connector);
        return connector.getId();
    }

    @Override
    public List<ConnectorVO> getConnectorsByDeviceId(Long deviceId) {
        List<Connector> connectors = connectorMapper.findByDeviceId(deviceId);
        return connectors.stream()
                .map(this::convertToConnectorVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceStatus(Long id, String deviceStatus) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND, "设备不存在");
        }

        device.setDeviceStatus(deviceStatus);
        deviceMapper.updateById(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConnectorStatus(Long connectorId, String connectorStatus) {
        Connector connector = connectorMapper.selectById(connectorId);
        if (connector == null) {
            throw new BusinessException(ResultCode.CONNECTOR_NOT_FOUND, "充电枪不存在");
        }

        connector.setConnectorStatus(connectorStatus);
        connectorMapper.updateById(connector);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deviceOnline(Long id) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND, "设备不存在");
        }

        device.setDeviceStatus("IDLE");
        device.setLastOnlineTime(LocalDateTime.now());
        deviceMapper.updateById(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deviceOffline(Long id) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND, "设备不存在");
        }

        device.setDeviceStatus("OFFLINE");
        device.setLastOfflineTime(LocalDateTime.now());
        deviceMapper.updateById(device);
    }

    /**
     * 生成设备编码
     */
    private String generateDeviceCode() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "DV" + date + random;
    }

    /**
     * 转换为VO
     */
    private DeviceVO convertToVO(Device device) {
        DeviceVO vo = new DeviceVO();
        BeanUtils.copyProperties(device, vo);
        vo.setDeviceStatusDisplay(DEVICE_STATUS_MAP.getOrDefault(device.getDeviceStatus(), device.getDeviceStatus()));
        return vo;
    }

    /**
     * 转换为充电枪VO
     */
    private ConnectorVO convertToConnectorVO(Connector connector) {
        ConnectorVO vo = new ConnectorVO();
        BeanUtils.copyProperties(connector, vo);
        vo.setConnectorStatusDisplay(
                CONNECTOR_STATUS_MAP.getOrDefault(connector.getConnectorStatus(), connector.getConnectorStatus()));
        return vo;
    }
}
