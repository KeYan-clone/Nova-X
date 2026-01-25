package com.novax.common.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于记录用户操作日志
 *
 * @author Nova-X
 * @since 2026-01-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 操作描述
     */
    String value() default "";

    /**
     * 操作类型
     */
    OperationType type() default OperationType.OTHER;

    /**
     * 是否保存请求参数
     */
    boolean saveParams() default true;

    /**
     * 是否保存响应结果
     */
    boolean saveResult() default true;

    /**
     * 操作类型枚举
     */
    enum OperationType {
        /**
         * 查询
         */
        SELECT,

        /**
         * 新增
         */
        INSERT,

        /**
         * 修改
         */
        UPDATE,

        /**
         * 删除
         */
        DELETE,

        /**
         * 导入
         */
        IMPORT,

        /**
         * 导出
         */
        EXPORT,

        /**
         * 登录
         */
        LOGIN,

        /**
         * 登出
         */
        LOGOUT,

        /**
         * 其他
         */
        OTHER
    }
}
