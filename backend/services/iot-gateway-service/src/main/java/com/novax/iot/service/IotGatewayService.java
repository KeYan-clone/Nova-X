package com.novax.iot.service;

import com.novax.iot.dto.DeviceDataDTO;

public interface IotGatewayService {

    Boolean receiveDeviceData(DeviceDataDTO dto);

    Boolean sendCommand(String deviceCode, String command, Object params);

    Boolean checkDeviceOnline(String deviceCode);
}
