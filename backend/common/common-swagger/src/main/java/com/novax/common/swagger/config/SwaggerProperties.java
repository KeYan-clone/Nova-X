package com.novax.common.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger/Knife4j 配置属性
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /**
     * 是否启用 Swagger
     */
    private boolean enable = true;

    /**
     * 分组名称
     */
    private String groupName = "default";

    /**
     * 标题
     */
    private String title = "API Documentation";

    /**
     * 描述
     */
    private String description = "API documentation";

    /**
     * 版本
     */
    private String version = "v1";

    /**
     * 服务条款地址
     */
    private String termsOfService;

    /**
     * 服务地址（可选）
     */
    private String serverUrl;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 联系人主页
     */
    private String contactUrl;

    /**
     * 许可证名称
     */
    private String licenseName;

    /**
     * 许可证地址
     */
    private String licenseUrl;

    /**
     * 扫描路径
     */
    private List<String> pathsToMatch = new ArrayList<>(List.of("/api/**"));

    /**
     * 扫描包
     */
    private List<String> packagesToScan = new ArrayList<>();

    /**
     * 是否启用鉴权 Header
     */
    private boolean bearerEnabled = true;

    /**
     * 鉴权 Header 名称
     */
    private String authHeaderName = "Authorization";

    /**
     * 鉴权 Header 描述
     */
    private String authHeaderDescription = "Bearer token";
}
