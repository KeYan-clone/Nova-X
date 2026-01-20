package com.novax.data.search.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 搜索服务配置
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.novax.data.search.repository")
public class SearchConfig {
}
