package com.novax.drvpp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novax.common.core.exception.BusinessException;
import com.novax.drvpp.dto.CreateEventDTO;
import com.novax.drvpp.entity.DemandResponseEvent;
import com.novax.drvpp.entity.VppDevice;
import com.novax.drvpp.mapper.DemandResponseEventMapper;
import com.novax.drvpp.mapper.VppDeviceMapper;
import com.novax.drvpp.service.DrVppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrVppServiceImpl implements DrVppService {

    private final DemandResponseEventMapper eventMapper;
    private final VppDeviceMapper deviceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandResponseEvent createEvent(CreateEventDTO dto) {
        log.info("创建需求响应事件: {}", dto.getEventName());

        DemandResponseEvent event = new DemandResponseEvent();
        event.setEventNo(generateEventNo());
        event.setEventName(dto.getEventName());
        event.setEventType(dto.getEventType());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setTargetPowerReduction(dto.getTargetPowerReduction());
        event.setIncentivePrice(dto.getIncentivePrice());
        event.setEventStatus("PLANNED");
        event.setParticipantCount(0);
        event.setActualPowerReduction(BigDecimal.ZERO);
        event.setTotalIncentive(BigDecimal.ZERO);
        event.setDescription(dto.getDescription());

        eventMapper.insert(event);

        log.info("需求响应事件创建成功, 事件编号: {}", event.getEventNo());
        return event;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startEvent(Long eventId) {
        DemandResponseEvent event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new BusinessException("事件不存在");
        }

        if (!"PLANNED".equals(event.getEventStatus())) {
            throw new BusinessException("事件状态不是计划中");
        }

        List<VppDevice> devices = getAvailableDevices();
        event.setParticipantCount(devices.size());
        event.setEventStatus("ACTIVE");

        eventMapper.updateById(event);
        log.info("需求响应事件已启动, 事件ID: {}, 参与设备数: {}", eventId, devices.size());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeEvent(Long eventId) {
        DemandResponseEvent event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new BusinessException("事件不存在");
        }

        BigDecimal actualReduction = event.getTargetPowerReduction().multiply(new BigDecimal("0.85"));
        BigDecimal totalIncentive = actualReduction.multiply(event.getIncentivePrice());

        event.setActualPowerReduction(actualReduction);
        event.setTotalIncentive(totalIncentive);
        event.setEventStatus("COMPLETED");

        eventMapper.updateById(event);
        log.info("需求响应事件已完成, 事件ID: {}, 实际削减: {}kW, 总激励: {}元",
                eventId, actualReduction, totalIncentive);
        return true;
    }

    @Override
    public List<VppDevice> getAvailableDevices() {
        LambdaQueryWrapper<VppDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VppDevice::getIsOnline, true)
                .eq(VppDevice::getIsAdjustable, true);
        return deviceMapper.selectList(wrapper);
    }

    @Override
    public List<DemandResponseEvent> getActiveEvents() {
        LambdaQueryWrapper<DemandResponseEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DemandResponseEvent::getEventStatus, "PLANNED", "ACTIVE")
                .orderByDesc(DemandResponseEvent::getStartTime);
        return eventMapper.selectList(wrapper);
    }

    @Override
    public DemandResponseEvent getEventById(Long eventId) {
        return eventMapper.selectById(eventId);
    }

    private String generateEventNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%03d", new Random().nextInt(1000));
        return "DR" + timestamp + random;
    }
}
