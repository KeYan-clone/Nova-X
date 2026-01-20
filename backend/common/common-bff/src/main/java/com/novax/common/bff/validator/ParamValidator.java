package com.novax.common.bff.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * BFF 统一参数验证器
 * 所有 BFF 服务通用的参数校验逻辑
 */
@Component
public class ParamValidator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 验证分页参数
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     */
    public void validatePageParams(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            throw new IllegalArgumentException("页码必须大于0");
        }

        if (pageSize == null || pageSize < 1) {
            throw new IllegalArgumentException("每页大小必须大于0");
        }

        if (pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("每页大小不能超过" + MAX_PAGE_SIZE);
        }
    }

    /**
     * 验证日期范围
     * @param startDate 开始日期 (yyyy-MM-dd)
     * @param endDate 结束日期 (yyyy-MM-dd)
     */
    public void validateDateRange(String startDate, String endDate) {
        if (!StringUtils.hasText(startDate) || !StringUtils.hasText(endDate)) {
            throw new IllegalArgumentException("日期范围不能为空");
        }

        try {
            LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);

            if (start.isAfter(end)) {
                throw new IllegalArgumentException("开始日期不能晚于结束日期");
            }

            long days = ChronoUnit.DAYS.between(start, end);
            if (days > 365) {
                throw new IllegalArgumentException("日期范围不能超过365天");
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式错误，请使用 yyyy-MM-dd 格式");
        }
    }

    /**
     * 验证业务ID格式
     * @param id 业务ID
     * @param idType ID类型（用于错误提示）
     */
    public void validateBusinessId(String id, String idType) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException(idType + "不能为空");
        }

        // 验证ID长度（通常为18-20位）
        if (id.length() < 10 || id.length() > 30) {
            throw new IllegalArgumentException(idType + "格式错误");
        }

        // 验证是否包含非法字符
        if (!id.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException(idType + "只能包含字母、数字、下划线和连字符");
        }
    }

    /**
     * 验证经纬度
     */
    public void validateLocation(java.math.BigDecimal latitude, java.math.BigDecimal longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("经纬度不能为空");
        }

        if (latitude.compareTo(new java.math.BigDecimal("-90")) < 0 ||
            latitude.compareTo(new java.math.BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("纬度必须在-90到90之间");
        }

        if (longitude.compareTo(new java.math.BigDecimal("-180")) < 0 ||
            longitude.compareTo(new java.math.BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("经度必须在-180到180之间");
        }
    }
}
