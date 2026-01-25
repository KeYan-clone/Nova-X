package com.novax.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novax.common.core.exception.BusinessException;
import com.novax.common.core.page.PageQuery;
import com.novax.common.core.page.PageResult;
import com.novax.common.core.result.ResultCode;
import com.novax.common.core.util.IdGenerator;
import com.novax.station.domain.dto.NearbyStationQueryDTO;
import com.novax.station.domain.dto.StationCreateDTO;
import com.novax.station.domain.dto.StationUpdateDTO;
import com.novax.station.domain.entity.Station;
import com.novax.station.domain.vo.StationVO;
import com.novax.station.mapper.StationMapper;
import com.novax.station.service.StationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 充电站服务实现类
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StationServiceImpl implements StationService {

    private final StationMapper stationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createStation(StationCreateDTO dto) {
        log.info("创建充电站: {}", dto.getStationName());

        // 生成站点编号
        String stationCode = generateStationCode();

        // 转换实体
        Station station = BeanUtil.copyProperties(dto, Station.class);
        station.setStationCode(stationCode);
        station.setStationStatus("NORMAL");
        station.setTotalPiles(0);
        station.setAvailablePiles(0);
        station.setTotalConnectors(0);
        station.setAvailableConnectors(0);
        station.setRating(BigDecimal.valueOf(5.0));
        station.setReviewCount(0);

        stationMapper.insert(station);

        log.info("充电站创建成功: stationCode={}, stationId={}", stationCode, station.getId());
        return station.getId();
    }

    @Override
    public StationVO getStation(Long stationId) {
        Station station = stationMapper.selectById(stationId);
        if (station == null) {
            throw new BusinessException(ResultCode.STATION_NOT_FOUND, "充电站不存在");
        }
        return convertToVO(station);
    }

    @Override
    public StationVO getStationByCode(String stationCode) {
        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Station::getStationCode, stationCode);
        Station station = stationMapper.selectOne(wrapper);
        if (station == null) {
            throw new BusinessException(ResultCode.STATION_NOT_FOUND, "充电站不存在");
        }
        return convertToVO(station);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStation(Long stationId, StationUpdateDTO dto) {
        log.info("更新充电站: stationId={}", stationId);

        Station station = stationMapper.selectById(stationId);
        if (station == null) {
            throw new BusinessException(ResultCode.STATION_NOT_FOUND, "充电站不存在");
        }

        // 更新非空字段
        BeanUtil.copyProperties(dto, station, "id", "stationCode", "operatorId", "operatorName");

        stationMapper.updateById(station);
        log.info("充电站更新成功: stationId={}", stationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStation(Long stationId) {
        log.info("删除充电站: stationId={}", stationId);

        Station station = stationMapper.selectById(stationId);
        if (station == null) {
            throw new BusinessException(ResultCode.STATION_NOT_FOUND, "充电站不存在");
        }

        // 检查是否有设备
        if (station.getTotalPiles() > 0) {
            throw new BusinessException(ResultCode.CONFLICT, "该站点还有充电桩，无法删除");
        }

        stationMapper.deleteById(stationId);
        log.info("充电站删除成功: stationId={}", stationId);
    }

    @Override
    public PageResult<StationVO> listStations(PageQuery pageQuery) {
        Page<Station> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        Page<Station> stationPage = stationMapper.selectPage(page, null);

        List<StationVO> voList = stationPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(pageQuery.getPage(), pageQuery.getPageSize(), stationPage.getTotal(), voList);
    }

    @Override
    public List<StationVO> findNearbyStations(NearbyStationQueryDTO dto) {
        log.info("查询附近充电站: lng={}, lat={}, radius={}km",
                dto.getLongitude(), dto.getLatitude(), dto.getRadius());

        List<Station> stations = stationMapper.findNearbyStations(
                dto.getLongitude(),
                dto.getLatitude(),
                dto.getRadius(),
                dto.getStationType(),
                dto.getOnlyAvailable());

        return stations.stream()
                .map(station -> {
                    StationVO vo = convertToVO(station);
                    // 计算距离（这里简化处理，实际应该从SQL查询结果中获取）
                    BigDecimal distance = calculateDistance(
                            dto.getLongitude(), dto.getLatitude(),
                            station.getLongitude(), station.getLatitude());
                    vo.setDistance(distance);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStationStatus(Long stationId, String status) {
        log.info("更新站点状态: stationId={}, status={}", stationId, status);

        Station station = stationMapper.selectById(stationId);
        if (station == null) {
            throw new BusinessException(ResultCode.STATION_NOT_FOUND, "充电站不存在");
        }

        station.setStationStatus(status);
        stationMapper.updateById(station);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStationStats(Long stationId) {
        log.info("更新站点统计: stationId={}", stationId);

        // TODO: 实际应该统计device-service中的桩和枪数量
        // 这里仅作为示例
        Station station = stationMapper.selectById(stationId);
        if (station == null) {
            throw new BusinessException(ResultCode.STATION_NOT_FOUND, "充电站不存在");
        }

        // 模拟统计（实际应该通过Feign调用设备服务）
        stationMapper.updateById(station);
    }

    /**
     * 生成站点编号
     * 格式: ST + 年月日 + 4位序号
     */
    private String generateStationCode() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = Math.abs(IdGenerator.generateId() % 10000);
        return String.format("ST%s%04d", date, seq);
    }

    /**
     * 转换为VO
     */
    private StationVO convertToVO(Station station) {
        StationVO vo = BeanUtil.copyProperties(station, StationVO.class);
        // 脱敏手机号
        if (station.getContactPhone() != null) {
            vo.setContactPhone(DesensitizedUtil.mobilePhone(station.getContactPhone()));
        }
        return vo;
    }

    /**
     * 计算两点之间的距离（Haversine公式）
     *
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 纬度2
     * @return 距离（公里）
     */
    private BigDecimal calculateDistance(BigDecimal lon1, BigDecimal lat1,
            BigDecimal lon2, BigDecimal lat2) {
        double earthRadius = 6371; // 地球半径（公里）

        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue()))
                        * Math.cos(Math.toRadians(lat2.doubleValue()))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP);
    }
}
