package com.novax.common.log.config;

import com.novax.common.log.aspect.LogAspect;
import com.novax.common.log.filter.TraceIdFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置类
 * 自动配置日志相关的Bean
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Configuration
@Slf4j
public class LogAutoConfiguration {

    /**
     * 注册TraceID过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public TraceIdFilter traceIdFilter() {
        log.info("初始化TraceID过滤器");
        return new TraceIdFilter();
    }

    /**
     * 注册日志切面
     */
    @Bean
    @ConditionalOnMissingBean
    public LogAspect logAspect() {
        log.info("初始化日志切面");
        return new LogAspect();
    }
}
