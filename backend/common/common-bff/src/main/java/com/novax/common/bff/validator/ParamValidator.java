package com.novax.common.bff.validator;

import org.springframework.stereotype.Component;

/**
 * BFF 统一参数验证器
 * 所有 BFF 服务通用的参数校验逻辑
 */
@Component
public class ParamValidator {

    /**
     * 验证分页参数
     */
    public void validatePageParams(Integer pageNum, Integer pageSize) {
        // TODO: 实现分页参数验证
    }

    /**
     * 验证日期范围
     */
    public void validateDateRange(String startDate, String endDate) {
        // TODO: 实现日期范围验证
    }

    /**
     * 验证业务ID格式
     */
    public void validateBusinessId(String id, String idType) {
        // TODO: 实现业务ID验证
    }
}
