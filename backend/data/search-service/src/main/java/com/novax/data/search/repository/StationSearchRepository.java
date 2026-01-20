package com.novax.data.search.repository;

import com.novax.data.search.document.StationSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 充电站搜索仓库
 */
public interface StationSearchRepository extends ElasticsearchRepository<StationSearchDocument, Long> {
}
