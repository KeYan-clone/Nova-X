package com.novax.station.service;

import com.novax.common.core.page.PageQuery;
import com.novax.common.core.page.PageResult;
import com.novax.station.domain.dto.NearbyStationQueryDTO;
import com.novax.station.domain.dto.StationCreateDTO;
import com.novax.station.domain.dto.StationUpdateDTO;
import com.novax.station.domain.vo.StationVO;

import java.util.List;

/**
 * 充电站服务接口
 *
 * @author Nova-X
 * @since 2026-01-20
 */
public interface StationService {

    /**
     * 创建充电站
     *
     * @param dto 充电站信息
     * @return 站点ID
     */
    Long createStation(StationCreateDTO dto);

    /**
     * 获取充电站详情
     *
     * @param stationId 站点ID
     * @return 充电站信息
     */
    StationVO getStation(Long stationId);

    /**
     * 根据站点编号获取充电站
     *
     * @param stationCode 站点编号
     * @return 充电站信息
     */
    StationVO getStationByCode(String stationCode);

    /**
     * 更新充电站信息
     *
     * @param stationId 站点ID
     * @param dto 更新信息
     */
    void updateStation(Long stationId, StationUpdateDTO dto);

    /**
     * 删除充电站
     *
     * @param stationId 站点ID
     */
    void deleteStation(Long stationId);

    /**
     * 分页查询充电站列表
     *
     * @param pageQuery 分页参数
     * @return 充电站列表
     */
    PageResult<StationVO> listStations(PageQuery pageQuery);

    /**
     * 查询附近的充电站
     *
     * @param dto 查询条件
     * @return 充电站列表（包含距离）
     */
    List<StationVO> findNearbyStations(NearbyStationQueryDTO dto);

    /**
     * 更新站点状态
     *
     * @param stationId 站点ID
     * @param status 新状态
     */
    void updateStationStatus(Long stationId, String status);

    /**
     * 更新站点桩数和枪数统计
     *
     * @param stationId 站点ID
     */
    void updateStationStats(Long stationId);
}
