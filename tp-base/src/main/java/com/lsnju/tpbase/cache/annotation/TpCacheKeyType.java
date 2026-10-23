package com.lsnju.tpbase.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lsnju.tpbase.cache.enums.TpCacheTypeEnum;

/**
 *
 * @author lisong
 * @since 2026-10-22 16:38
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TpCacheKeyType {

    TpCacheTypeEnum value() default TpCacheTypeEnum.FULL;

    /**
     * Fixed cache-key segment when {@link #value()} is {@link TpCacheTypeEnum#CUSTOM}.
     */
    String customKey() default "";

}
