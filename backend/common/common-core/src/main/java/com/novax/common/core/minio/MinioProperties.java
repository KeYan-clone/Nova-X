package com.novax.common.core.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO 配置属性
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * MinIO 内网访问地址（服务间访问）
     */
    private String endpoint;

    /**
     * 对外访问地址（返回给客户端的 URL）
     */
    private String publicEndpoint;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 头像路径前缀
     */
    private String avatarPrefix = "avatars";
}
