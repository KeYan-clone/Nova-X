package com.novax.report.service;

import com.novax.report.vo.ChargingStatisticsVO;
import com.novax.report.vo.RevenueStatisticsVO;
import com.novax.report.vo.StationStatisticsVO;

import java.util.List;

public interface ReportService {

    ChargingStatisticsVO getChargingStatistics(String startDate, String endDate);

    RevenueStatisticsVO getRevenueStatistics(String startDate, String endDate);

    List<StationStatisticsVO> getStationStatistics(String startDate, String endDate);
}
