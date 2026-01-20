package com.novax.data.sync.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电会话 ES 文档
 */
@Data
@Document(indexName = "charging_sessions")
public class ChargingSessionDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String sessionId;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String userName;

    @Field(type = FieldType.Long)
    private Long stationId;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String stationName;

    @Field(type = FieldType.Long)
    private Long deviceId;

    @Field(type = FieldType.Keyword)
    private String deviceCode;

    @Field(type = FieldType.Double)
    private BigDecimal energy;

    @Field(type = FieldType.Integer)
    private Integer duration;

    @Field(type = FieldType.Double)
    private BigDecimal totalAmount;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
