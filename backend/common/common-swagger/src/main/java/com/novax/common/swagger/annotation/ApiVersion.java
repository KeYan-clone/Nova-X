package com.novax.common.swagger.annotation;

import java.lang.annotation.*;

/**
 * API版本注解
 * 用于标记API版本信息
 *
 * @author Nova-X
 * @since 2026-01-20
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    /**
     * API版本号
     */
    String value() default "v1";

    /**
     * 是否废弃
     */
    boolean deprecated() default false;

    /**
     * 废弃说明
     */
    String deprecatedReason() default "";
}
