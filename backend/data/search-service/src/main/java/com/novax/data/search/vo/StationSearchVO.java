package com.novax.data.search.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 充电站搜索结果VO
 */
@Data
public class StationSearchVO {

    private Long id;
    private String stationName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer totalDevices;
    private Integer availableDevices;
    private String status;
    private String operatorName;
    private String facilities;

    /**
     * 距离（公里）- 仅地理位置搜索时有值
     */
    private Double distance;

    /**
     * 搜索得分 - 用于排序
     */
    private Float score;
}
