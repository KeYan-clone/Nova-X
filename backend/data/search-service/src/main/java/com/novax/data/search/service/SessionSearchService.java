package com.novax.data.search.service;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.json.JsonData;
import com.novax.data.search.document.SessionSearchDocument;
import com.novax.data.search.vo.SessionSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 充电会话搜索服务
 * 使用 Elasticsearch 8.x 新版 API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 根据用户ID搜索充电会话
     */
    public List<SessionSearchVO> searchByUserId(Long userId, int pageNum, int pageSize) {
        log.info("搜索用户充电会话: userId={}", userId);

        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .term(t -> t
                                    .field("userId")
                                    .value(userId)))
                    .withSort(s -> s
                            .field(f -> f
                                    .field("startTime")
                                    .order(SortOrder.Desc)))
                    .withPageable(PageRequest.of(pageNum - 1, pageSize))
                    .build();

            SearchHits<SessionSearchDocument> searchHits = elasticsearchOperations.search(searchQuery,
                    SessionSearchDocument.class);

            return convertToVO(searchHits);

        } catch (Exception e) {
            log.error("搜索用户充电会话失败: userId={}", userId, e);
            return new ArrayList<>();
        }
    }

    /**
     * 根据会话ID搜索
     */
    public SessionSearchVO searchBySessionId(String sessionId) {
        log.info("搜索充电会话: sessionId={}", sessionId);

        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .term(t -> t
                                    .field("sessionId")
                                    .value(sessionId)))
                    .build();

            SearchHits<SessionSearchDocument> searchHits = elasticsearchOperations.search(searchQuery,
                    SessionSearchDocument.class);

            List<SessionSearchVO> results = convertToVO(searchHits);
            return results.isEmpty() ? null : results.get(0);

        } catch (Exception e) {
            log.error("搜索充电会话失败: sessionId={}", sessionId, e);
            return null;
        }
    }

    /**
     * 根据充电站ID搜索会话
     */
    public List<SessionSearchVO> searchByStationId(Long stationId, String status,
            int pageNum, int pageSize) {
        log.info("搜索充电站会话: stationId={}, status={}", stationId, status);

        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> {
                        if (status != null && !status.isEmpty()) {
                            return q.bool(b -> b
                                    .must(m -> m
                                            .term(t -> t
                                                    .field("stationId")
                                                    .value(stationId)))
                                    .filter(f -> f
                                            .term(t -> t
                                                    .field("status")
                                                    .value(status))));
                        } else {
                            return q.term(t -> t
                                    .field("stationId")
                                    .value(stationId));
                        }
                    })
                    .withSort(s -> s
                            .field(f -> f
                                    .field("startTime")
                                    .order(SortOrder.Desc)))
                    .withPageable(PageRequest.of(pageNum - 1, pageSize))
                    .build();

            SearchHits<SessionSearchDocument> searchHits = elasticsearchOperations.search(searchQuery,
                    SessionSearchDocument.class);

            return convertToVO(searchHits);

        } catch (Exception e) {
            log.error("搜索充电站会话失败: stationId={}", stationId, e);
            return new ArrayList<>();
        }
    }

    /**
     * 按时间范围搜索
     */
    public List<SessionSearchVO> searchByTimeRange(LocalDateTime startTime, LocalDateTime endTime,
            int pageNum, int pageSize) {
        log.info("按时间范围搜索: {} 至 {}", startTime, endTime);

        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .range(r -> r
                                    .date(d -> d 
                                            .field("startTime")
                                            .gte(startTime.toString())
                                            .lte(endTime.toString()))))
                    .withSort(s -> s
                            .field(f -> f
                                    .field("startTime")
                                    .order(SortOrder.Desc)))
                    .withPageable(PageRequest.of(pageNum - 1, pageSize))
                    .build();

            SearchHits<SessionSearchDocument> searchHits = elasticsearchOperations.search(searchQuery,
                    SessionSearchDocument.class);

            return convertToVO(searchHits);

        } catch (Exception e) {
            log.error("按时间范围搜索失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 转换为VO
     */
    private List<SessionSearchVO> convertToVO(SearchHits<SessionSearchDocument> searchHits) {
        List<SessionSearchVO> results = new ArrayList<>();

        for (SearchHit<SessionSearchDocument> hit : searchHits) {
            SessionSearchDocument doc = hit.getContent();

            SessionSearchVO vo = new SessionSearchVO();
            vo.setId(doc.getId());
            vo.setSessionId(doc.getSessionId());
            vo.setUserId(doc.getUserId());
            vo.setUserName(doc.getUserName());
            vo.setStationId(doc.getStationId());
            vo.setStationName(doc.getStationName());
            vo.setDeviceCode(doc.getDeviceCode());
            vo.setEnergy(doc.getEnergy());
            vo.setTotalAmount(doc.getTotalAmount());
            vo.setStatus(doc.getStatus());
            vo.setStartTime(doc.getStartTime());
            vo.setEndTime(doc.getEndTime());
            vo.setScore(hit.getScore());

            results.add(vo);
        }

        return results;
    }
}
