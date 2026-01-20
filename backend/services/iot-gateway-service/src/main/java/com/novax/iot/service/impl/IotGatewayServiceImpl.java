package com.novax.iot.service.impl;

import com.novax.iot.dto.DeviceDataDTO;
import com.novax.iot.service.IotGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class IotGatewayServiceImpl implements IotGatewayService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String DEVICE_ONLINE_KEY = "device:online:";
    private static final String DEVICE_DATA_TOPIC = "device-data";

    @Override
    public Boolean receiveDeviceData(DeviceDataDTO dto) {
        log.info("接收设备数据, 设备编号: {}, 类型: {}", dto.getDeviceCode(), dto.getDataType());

        String key = DEVICE_ONLINE_KEY + dto.getDeviceCode();
        redisTemplate.opsForValue().set(key, "1", 5, TimeUnit.MINUTES);

        kafkaTemplate.send(DEVICE_DATA_TOPIC, dto.getDeviceCode(), dto);

        return true;
    }

    @Override
    public Boolean sendCommand(String deviceCode, String command, Object params) {
        log.info("发送设备指令, 设备编号: {}, 指令: {}", deviceCode, command);
        return true;
    }

    @Override
    public Boolean checkDeviceOnline(String deviceCode) {
        String key = DEVICE_ONLINE_KEY + deviceCode;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
