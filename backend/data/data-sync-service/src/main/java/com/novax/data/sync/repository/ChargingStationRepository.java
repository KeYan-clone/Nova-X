package com.novax.data.sync.repository;

import com.novax.data.sync.document.ChargingStationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 充电站 ES 仓库
 */
public interface ChargingStationRepository extends ElasticsearchRepository<ChargingStationDocument, Long> {
}
