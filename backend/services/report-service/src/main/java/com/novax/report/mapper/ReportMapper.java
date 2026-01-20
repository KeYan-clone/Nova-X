package com.novax.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ReportMapper {

    Map<String, Object> getChargingStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);

    Map<String, Object> getRevenueStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
