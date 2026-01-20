package com.novax.scheduling.service;

import com.novax.scheduling.dto.CreateReservationDTO;
import com.novax.scheduling.entity.ChargingReservation;
import com.novax.scheduling.vo.AvailableDeviceVO;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulingService {

    List<AvailableDeviceVO> getAvailableDevices(Long stationId, LocalDateTime startTime, LocalDateTime endTime);

    ChargingReservation createReservation(CreateReservationDTO dto);

    Boolean cancelReservation(Long reservationId, String reason);

    List<ChargingReservation> getUserReservations(Long userId);

    ChargingReservation getById(Long reservationId);
}
