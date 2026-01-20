package com.novax.station.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 附近站点查询DTO
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Data
@Schema(description = "附近站点查询请求")
public class NearbyStationQueryDTO {

    @Schema(description = "当前位置经度", example = "116.404", required = true)
    private BigDecimal longitude;

    @Schema(description = "当前位置纬度", example = "39.915", required = true)
    private BigDecimal latitude;

    @Schema(description = "搜索半径（公里）", example = "5", defaultValue = "5")
    private Integer radius = 5;

    @Schema(description = "站点类型", example = "PUBLIC", allowableValues = {"PUBLIC", "PRIVATE", "BUS", "TAXI"})
    private String stationType;

    @Schema(description = "是否只显示可用站点", example = "true", defaultValue = "false")
    private Boolean onlyAvailable = false;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "20", defaultValue = "20")
    private Integer pageSize = 20;
}
