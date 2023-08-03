package com.lsnju.tpbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lsnju.tpbase.autoconfigure.TpFreemarkerAutoConfiguration;

/**
 *
 * @author ls
 * @since 2023-08-03 12:47:54
 * @version V1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({TpFreemarkerAutoConfiguration.class})
public @interface EnableFreemarkerConfig {
}
