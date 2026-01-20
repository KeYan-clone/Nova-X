package com.novax.common.bff.config;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.novax.common.bff.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * BFF 公共配置
 * 配置统一的超时、重试和熔断策略
 */
@Configuration
@RequiredArgsConstructor
public class BffConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    /**
     * 配置 Feign 统一超时时间
     * 连接超时：5秒，读取超时：10秒
     */
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(
            5, TimeUnit.SECONDS,  // 连接超时
            10, TimeUnit.SECONDS, // 读取超时
            true                  // 跟随重定向
        );
    }

    /**
     * 配置 Feign 重试策略
     * 最多重试2次，初始间隔100ms，最大间隔1s
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(
            100,  // 初始间隔（毫秒）
            1000, // 最大间隔（毫秒）
            2     // 最大重试次数
        );
    }

    /**
     * 注册认证拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/health", "/actuator/**");
    }
}
