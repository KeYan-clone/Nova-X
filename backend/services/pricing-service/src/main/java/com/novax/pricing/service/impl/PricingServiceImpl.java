package com.novax.pricing.service.impl;

import com.novax.pricing.entity.PricingTemplate;
import com.novax.pricing.mapper.PricingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 定价服务实现
 */
@Service
@RequiredArgsConstructor
public class PricingServiceImpl {

    private final PricingMapper pricingMapper;

    /**
     * 计算充电费用
     */
    public BigDecimal calculateChargingCost(Long stationId, BigDecimal energy, LocalDateTime startTime, LocalDateTime endTime) {
        PricingTemplate template = pricingMapper.findActiveTemplateByStationId(stationId);

        if (template == null) {
            // 使用默认定价
            return energy.multiply(BigDecimal.valueOf(1.5)); // 默认1.5元/kWh
        }

        // 分时段计算
        BigDecimal electricityCost = calculateElectricityCost(energy, startTime, template);
        BigDecimal serviceCost = energy.multiply(template.getServiceFee());

        return electricityCost.add(serviceCost);
    }

    /**
     * 分时电价计算
     */
    private BigDecimal calculateElectricityCost(BigDecimal energy, LocalDateTime time, PricingTemplate template) {
        int hour = time.getHour();
        BigDecimal price;

        // 尖峰时段: 10:00-15:00, 18:00-21:00
        if ((hour >= 10 && hour < 15) || (hour >= 18 && hour < 21)) {
            price = template.getPeakPrice();
        }
        // 平时段: 07:00-10:00, 15:00-18:00, 21:00-23:00
        else if ((hour >= 7 && hour < 10) || (hour >= 15 && hour < 18) || (hour >= 21 && hour < 23)) {
            price = template.getNormalPrice();
        }
        // 谷时段: 23:00-07:00
        else {
            price = template.getValleyPrice();
        }

        return energy.multiply(price);
    }
}
