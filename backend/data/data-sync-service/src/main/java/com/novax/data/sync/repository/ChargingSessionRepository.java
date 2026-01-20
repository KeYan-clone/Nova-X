package com.novax.data.sync.repository;

import com.novax.data.sync.document.ChargingSessionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 充电会话 ES 仓库
 */
public interface ChargingSessionRepository extends ElasticsearchRepository<ChargingSessionDocument, Long> {
}
