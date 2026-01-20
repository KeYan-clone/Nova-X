package com.novax.common.log.annotation;

import java.lang.annotation.*;

/**
 * Log annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value() default "";
}
