package com.novax.data.search.service;

import co.elastic.clients.elasticsearch._types.DistanceUnit;
import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import com.novax.data.search.document.StationSearchDocument;
import com.novax.data.search.vo.StationSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 充电站搜索服务
 * 使用 Elasticsearch 8.x 新版 API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StationSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 全文搜索充电站
     *
     * @param keyword  搜索关键词
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    public List<StationSearchVO> searchStations(String keyword, int pageNum, int pageSize) {
        log.info("全文搜索充电站: {}", keyword);

        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .bool(b -> b
                                    .must(m -> m
                                            .multiMatch(mm -> mm
                                                    .query(keyword)
                                                    .fields("stationName^3", "address^2", "facilities^1")))
                                    .filter(f -> f
                                            .term(t -> t
                                                    .field("status")
                                                    .value("OPERATING")))))
                    .withPageable(PageRequest.of(pageNum - 1, pageSize))
                    .build();

            SearchHits<StationSearchDocument> searchHits = elasticsearchOperations.search(searchQuery,
                    StationSearchDocument.class);

            return convertToVO(searchHits);

        } catch (Exception e) {
            log.error("全文搜索失败: {}", keyword, e);
            return new ArrayList<>();
        }
    }

    /**
     * 地理位置附近搜索
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param radius    半径（公里）
     * @param pageSize  返回数量
     */
    public List<StationSearchVO> searchNearby(BigDecimal latitude, BigDecimal longitude,
            int radius, int pageSize) {
        log.info("附近搜索: lat={}, lon={}, radius={}km", latitude, longitude, radius);

        try {
            double lat = latitude.doubleValue();
            double lon = longitude.doubleValue();

            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .bool(b -> b
                                    .must(m -> m
                                            .geoDistance(g -> g
                                                    .field("location")
                                                    .distance(radius + "km")
                                                    .distanceType(GeoDistanceType.Arc)
                                                    .location(l -> l
                                                            .latlon(ll -> ll
                                                                    .lat(lat)
                                                                    .lon(lon)))))
                                    .filter(f -> f
                                            .term(t -> t
                                                    .field("status")
                                                    .value("OPERATING")))))
                    .withSort(s -> s
                            .geoDistance(g -> g
                                    .field("location")
                                    .location(l -> l
                                            .latlon(ll -> ll.lat(lat).lon(lon)))
                                    .unit(DistanceUnit.Kilometers)
                                    .order(SortOrder.Asc)))
                    .withPageable(PageRequest.of(0, pageSize))
                    .build();

            SearchHits<StationSearchDocument> searchHits = elasticsearchOperations.search(searchQuery,
                    StationSearchDocument.class);

            List<StationSearchVO> results = convertToVO(searchHits);

            // 计算距离
            for (int i = 0; i < results.size(); i++) {
                SearchHit<StationSearchDocument> hit = searchHits.getSearchHits().get(i);
                List<Object> sortValues = hit.getSortValues();
                if (!sortValues.isEmpty() && sortValues.get(0) instanceof Number) {
                    results.get(i).setDistance(((Number) sortValues.get(0)).doubleValue());
                }
            }

            return results;

        } catch (Exception e) {
            log.error("附近搜索失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 高级筛选搜索
     *
     * @param keyword             关键词（可选）
     * @param operatorName        运营商（可选）
     * @param minAvailableDevices 最少可用设备数
     */
    public List<StationSearchVO> advancedSearch(String keyword, String operatorName,
            Integer minAvailableDevices, int pageSize) {
        log.info("高级搜索: keyword={}, operator={}, minDevices={}",
                keyword, operatorName, minAvailableDevices);

        try {
            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .bool(b -> {
                                // 关键词查询
                                if (keyword != null && !keyword.isEmpty()) {
                                    b.must(m -> m
                                            .multiMatch(mm -> mm
                                                    .query(keyword)
                                                    .fields("stationName^3", "address^2")));
                                }

                                // 运营商筛选
                                if (operatorName != null && !operatorName.isEmpty()) {
                                    b.filter(f -> f
                                            .term(t -> t
                                                    .field("operatorName")
                                                    .value(operatorName)));
                                }

                                // 可用设备数筛选
                                if (minAvailableDevices != null && minAvailableDevices > 0) {
                                    b.filter(f -> f
                                            .range(r -> r
                                                .number(n -> n
                                                    .field("availableDevices")
                                                    .gte(minAvailableDevices.doubleValue()))));
                                }

                                // 状态筛选
                                b.filter(f -> f
                                        .term(t -> t
                                                .field("status")
                                                .value("OPERATING")));

                                return b;
                            }))
                    .withPageable(PageRequest.of(0, pageSize))
                    .build();

            SearchHits<StationSearchDocument> searchHits = elasticsearchOperations.search(searchQuery,
                    StationSearchDocument.class);

            return convertToVO(searchHits);

        } catch (Exception e) {
            log.error("高级搜索失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 转换为VO
     */
    private List<StationSearchVO> convertToVO(SearchHits<StationSearchDocument> searchHits) {
        List<StationSearchVO> results = new ArrayList<>();

        for (SearchHit<StationSearchDocument> hit : searchHits) {
            StationSearchDocument doc = hit.getContent();

            StationSearchVO vo = new StationSearchVO();
            vo.setId(doc.getId());
            vo.setStationName(doc.getStationName());
            vo.setAddress(doc.getAddress());

            if (doc.getLocation() != null) {
                vo.setLatitude(BigDecimal.valueOf(doc.getLocation().getLat()));
                vo.setLongitude(BigDecimal.valueOf(doc.getLocation().getLon()));
            }

            vo.setTotalDevices(doc.getTotalDevices());
            vo.setAvailableDevices(doc.getAvailableDevices());
            vo.setStatus(doc.getStatus());
            vo.setOperatorName(doc.getOperatorName());
            vo.setFacilities(doc.getFacilities());
            vo.setScore(hit.getScore());

            results.add(vo);
        }

        return results;
    }
}
