package com.lsnju.tpbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author ls
 * @since 2023-08-03 12:53:04
 * @version V1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableTpBaseConfig
@EnableFilterBaseConfig
@EnableFilterProfilerConfig
@EnableDigestConfig
@EnableAppWideExceptionHandler
public @interface EnableTpBase {
}
