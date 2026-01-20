package com.novax.report.service.impl;

import com.novax.report.mapper.ReportMapper;
import com.novax.report.service.ReportService;
import com.novax.report.vo.ChargingStatisticsVO;
import com.novax.report.vo.RevenueStatisticsVO;
import com.novax.report.vo.StationStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public ChargingStatisticsVO getChargingStatistics(String startDate, String endDate) {
        log.info("获取充电统计, 开始日期: {}, 结束日期: {}", startDate, endDate);

        Map<String, Object> result = reportMapper.getChargingStatistics(startDate, endDate);

        ChargingStatisticsVO vo = new ChargingStatisticsVO();
        if (result != null) {
            vo.setTotalChargingCount(toLong(result.get("totalChargingCount")));
            vo.setTotalEnergy(toBigDecimal(result.get("totalEnergy")));
            vo.setTotalDuration(toLong(result.get("totalDuration")));
            vo.setTotalAmount(toBigDecimal(result.get("totalAmount")));
            vo.setAvgEnergy(toBigDecimal(result.get("avgEnergy")));
            vo.setAvgDuration(toLong(result.get("avgDuration")));
            vo.setDeviceUtilization(toBigDecimal(result.get("deviceUtilization")));
        }
        vo.setStatisticsDate(startDate + " ~ " + endDate);

        return vo;
    }

    @Override
    public RevenueStatisticsVO getRevenueStatistics(String startDate, String endDate) {
        log.info("获取收入统计, 开始日期: {}, 结束日期: {}", startDate, endDate);

        Map<String, Object> result = reportMapper.getRevenueStatistics(startDate, endDate);

        RevenueStatisticsVO vo = new RevenueStatisticsVO();
        if (result != null) {
            vo.setChargingRevenue(toBigDecimal(result.get("chargingRevenue")));
            vo.setServiceFeeRevenue(toBigDecimal(result.get("serviceFeeRevenue")));
            vo.setParkingRevenue(toBigDecimal(result.get("parkingRevenue")));
            vo.setTotalRevenue(toBigDecimal(result.get("totalRevenue")));
            vo.setRechargeAmount(toBigDecimal(result.get("rechargeAmount")));
            vo.setOrderCount(toLong(result.get("orderCount")));
        }
        vo.setStatisticsDate(startDate + " ~ " + endDate);

        return vo;
    }

    @Override
    public List<StationStatisticsVO> getStationStatistics(String startDate, String endDate) {
        log.info("获取充电站统计, 开始日期: {}, 结束日期: {}", startDate, endDate);

        List<StationStatisticsVO> list = new ArrayList<>();

        StationStatisticsVO vo1 = new StationStatisticsVO();
        vo1.setStationId(1L);
        vo1.setStationName("市中心充电站");
        vo1.setChargingCount(150L);
        vo1.setTotalEnergy(new BigDecimal("2500.50"));
        vo1.setRevenue(new BigDecimal("4500.80"));
        vo1.setDeviceCount(10);
        vo1.setOnlineDeviceCount(9);
        vo1.setFaultDeviceCount(1);
        list.add(vo1);

        StationStatisticsVO vo2 = new StationStatisticsVO();
        vo2.setStationId(2L);
        vo2.setStationName("商业区充电站");
        vo2.setChargingCount(200L);
        vo2.setTotalEnergy(new BigDecimal("3200.00"));
        vo2.setRevenue(new BigDecimal("5800.00"));
        vo2.setDeviceCount(15);
        vo2.setOnlineDeviceCount(15);
        vo2.setFaultDeviceCount(0);
        list.add(vo2);

        return list;
    }

    private Long toLong(Object obj) {
        if (obj == null) return 0L;
        return obj instanceof Number ? ((Number) obj).longValue() : 0L;
    }

    private BigDecimal toBigDecimal(Object obj) {
        if (obj == null) return BigDecimal.ZERO;
        return obj instanceof BigDecimal ? (BigDecimal) obj : new BigDecimal(obj.toString());
    }
}
