package com.novax.settlement.service;

import com.novax.settlement.dto.CreateSettlementDTO;
import com.novax.settlement.entity.SettlementOrder;

import java.util.List;

public interface SettlementService {

    SettlementOrder createSettlement(CreateSettlementDTO dto);

    Boolean confirmSettlement(Long settlementId);

    Boolean paySettlement(Long settlementId);

    SettlementOrder getById(Long settlementId);

    List<SettlementOrder> getPendingSettlements();
}
