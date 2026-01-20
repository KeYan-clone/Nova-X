package com.novax.data.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.novax.data.search.document.SessionSearchDocument;
import com.novax.data.search.vo.SessionSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
            Query userQuery = Query.of(q -> q
                .term(t -> t
                    .field("userId")
                    .value(userId)
                )
            );

            NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(userQuery)
                .withSort(Sort.by(Sort.Direction.DESC, "startTime"))
                .withPageable(PageRequest.of(pageNum - 1, pageSize))
                .build();

            SearchHits<SessionSearchDocument> searchHits =
                elasticsearchOperations.search(searchQuery, SessionSearchDocument.class);

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
            Query sessionQuery = Query.of(q -> q
                .term(t -> t
                    .field("sessionId")
                    .value(sessionId)
                )
            );

            NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(sessionQuery)
                .build();

            SearchHits<SessionSearchDocument> searchHits =
                elasticsearchOperations.search(searchQuery, SessionSearchDocument.class);

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
            Query stationQuery = Query.of(q -> q
                .term(t -> t
                    .field("stationId")
                    .value(stationId)
                )
            );

            Query finalQuery;
            if (status != null && !status.isEmpty()) {
                Query statusQuery = Query.of(q -> q
                    .term(t -> t
                        .field("status")
                        .value(status)
                    )
                );

                finalQuery = Query.of(q -> q
                    .bool(b -> b
                        .must(stationQuery)
                        .filter(statusQuery)
                    )
                );
            } else {
                finalQuery = stationQuery;
            }

            NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(finalQuery)
                .withSort(Sort.by(Sort.Direction.DESC, "startTime"))
                .withPageable(PageRequest.of(pageNum - 1, pageSize))
                .build();

            SearchHits<SessionSearchDocument> searchHits =
                elasticsearchOperations.search(searchQuery, SessionSearchDocument.class);

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
            Query timeQuery = Query.of(q -> q
                .range(r -> r
                    .field("startTime")
                    .gte(co.elastic.clients.json.JsonData.of(startTime.toString()))
                    .lte(co.elastic.clients.json.JsonData.of(endTime.toString()))
                )
            );

            NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(timeQuery)
                .withSort(Sort.by(Sort.Direction.DESC, "startTime"))
                .withPageable(PageRequest.of(pageNum - 1, pageSize))
                .build();

            SearchHits<SessionSearchDocument> searchHits =
                elasticsearchOperations.search(searchQuery, SessionSearchDocument.class);

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
