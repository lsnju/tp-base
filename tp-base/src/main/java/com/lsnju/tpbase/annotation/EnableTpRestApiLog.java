package com.lsnju.tpbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lsnju.tpbase.autoconfigure.TpRestApiLogConfiguration;

/**
 *
 * @author lsnju
 * @since 2025-09-29 18:05:17
 * @version V1.0
 * @since 3.1.19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({TpRestApiLogConfiguration.class})
public @interface EnableTpRestApiLog {
}
