package com.novax.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.scheduling.entity.ChargingReservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper extends BaseMapper<ChargingReservation> {
}
