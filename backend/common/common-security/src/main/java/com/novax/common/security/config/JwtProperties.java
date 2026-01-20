package com.novax.common.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * 密钥
     */
    private String secret = "nova-x-secret-key-change-in-production";

    /**
     * Token 有效期（秒）
     * 默认 24 小时
     */
    private Long expiration = 86400L;

    /**
     * Token 前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Token 请求头名称
     */
    private String headerName = "Authorization";

    /**
     * 刷新Token有效期（秒）
     * 默认 7 天
     */
    private Long refreshExpiration = 604800L;

    /**
     * 签发者
     */
    private String issuer = "nova-x";

    /**
     * 受众
     */
    private String audience = "nova-x-user";
}
