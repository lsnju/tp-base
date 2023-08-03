package com.lsnju.tpbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lsnju.tpbase.autoconfigure.TpServletFilterConfiguration;

/**
 *
 * @author ls
 * @since 2023-08-03 12:46:36
 * @version V1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(value = {TpServletFilterConfiguration.TpBaseFilterConfig.class})
public @interface EnableFilterBaseConfig {
}
