package com.lsnju.tpbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lsnju.tpbase.autoconfigure.TpSpringUtilsAutoConfiguration;

/**
 *
 * @author lis614
 * @since 2025-06-21 19:34:43
 * @version V1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({TpSpringUtilsAutoConfiguration.class})
public @interface EnableTpSpringUtils {
}
