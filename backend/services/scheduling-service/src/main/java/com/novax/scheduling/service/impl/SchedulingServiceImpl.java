package com.novax.scheduling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novax.common.core.exception.BusinessException;
import com.novax.scheduling.dto.CreateReservationDTO;
import com.novax.scheduling.entity.ChargingReservation;
import com.novax.scheduling.mapper.ReservationMapper;
import com.novax.scheduling.service.SchedulingService;
import com.novax.scheduling.vo.AvailableDeviceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

    private final ReservationMapper reservationMapper;

    @Override
    public List<AvailableDeviceVO> getAvailableDevices(Long stationId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("查询可用设备, 充电站ID: {}, 时间: {} - {}", stationId, startTime, endTime);

        List<AvailableDeviceVO> list = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            AvailableDeviceVO vo = new AvailableDeviceVO();
            vo.setDeviceId((long) i);
            vo.setDeviceCode("DV20260120" + String.format("%03d", i));
            vo.setDeviceName("充电桩" + i + "号");
            vo.setStationId(stationId);
            vo.setStationName("市中心充电站");
            vo.setMaxPower(new BigDecimal("60"));
            vo.setStatus("AVAILABLE");
            vo.setAvailabilityScore(90 + i);
            list.add(vo);
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChargingReservation createReservation(CreateReservationDTO dto) {
        log.info("创建预约, 用户ID: {}, 充电站ID: {}", dto.getUserId(), dto.getStationId());

        List<AvailableDeviceVO> devices = getAvailableDevices(
            dto.getStationId(),
            dto.getReservationStartTime(),
            dto.getReservationEndTime()
        );

        if (devices.isEmpty()) {
            throw new BusinessException("该时段没有可用设备");
        }

        AvailableDeviceVO selectedDevice = devices.get(0);

        ChargingReservation reservation = new ChargingReservation();
        reservation.setReservationNo(generateReservationNo());
        reservation.setUserId(dto.getUserId());
        reservation.setStationId(dto.getStationId());
        reservation.setStationName(selectedDevice.getStationName());
        reservation.setDeviceId(selectedDevice.getDeviceId());
        reservation.setDeviceCode(selectedDevice.getDeviceCode());
        reservation.setReservationStartTime(dto.getReservationStartTime());
        reservation.setReservationEndTime(dto.getReservationEndTime());
        reservation.setEstimatedEnergy(dto.getEstimatedEnergy());
        reservation.setReservationStatus("CONFIRMED");

        reservationMapper.insert(reservation);

        log.info("预约创建成功, 预约编号: {}", reservation.getReservationNo());
        return reservation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelReservation(Long reservationId, String reason) {
        ChargingReservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }

        if ("COMPLETED".equals(reservation.getReservationStatus()) ||
            "CANCELLED".equals(reservation.getReservationStatus())) {
            throw new BusinessException("预约已完成或已取消");
        }

        reservation.setReservationStatus("CANCELLED");
        reservation.setCancelReason(reason);

        reservationMapper.updateById(reservation);
        log.info("预约取消成功, 预约ID: {}", reservationId);
        return true;
    }

    @Override
    public List<ChargingReservation> getUserReservations(Long userId) {
        LambdaQueryWrapper<ChargingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChargingReservation::getUserId, userId)
                .orderByDesc(ChargingReservation::getCreateTime);
        return reservationMapper.selectList(wrapper);
    }

    @Override
    public ChargingReservation getById(Long reservationId) {
        return reservationMapper.selectById(reservationId);
    }

    private String generateReservationNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%03d", new Random().nextInt(1000));
        return "RS" + timestamp + random;
    }
}
