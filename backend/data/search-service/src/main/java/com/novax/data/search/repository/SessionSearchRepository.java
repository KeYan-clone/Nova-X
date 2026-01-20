package com.novax.data.search.repository;

import com.novax.data.search.document.SessionSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 充电会话搜索仓库
 */
public interface SessionSearchRepository extends ElasticsearchRepository<SessionSearchDocument, Long> {
}
