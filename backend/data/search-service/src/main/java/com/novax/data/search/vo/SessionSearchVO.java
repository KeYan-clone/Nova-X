package com.novax.data.search.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电会话搜索结果VO
 */
@Data
public class SessionSearchVO {

    private Long id;
    private String sessionId;
    private Long userId;
    private String userName;
    private Long stationId;
    private String stationName;
    private String deviceCode;
    private BigDecimal energy;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * 搜索得分
     */
    private Float score;
}
