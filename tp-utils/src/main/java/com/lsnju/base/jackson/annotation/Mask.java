package com.lsnju.base.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.fasterxml.jackson.databind.JsonSerializer;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:22
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mask {

    @AliasFor("serClass")
    Class<? extends JsonSerializer<?>>[] value() default {};

    @AliasFor("value")
    Class<? extends JsonSerializer<?>>[] serClass() default {};

    Type type() default Type.DEFAULT;

    enum Type {
        GID,
        PHONE,
        DEFAULT
    }

}
