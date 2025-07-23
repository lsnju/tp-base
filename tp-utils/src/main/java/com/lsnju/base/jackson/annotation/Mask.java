package com.lsnju.base.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

    Type type() default Type.DEFAULT;

    Class<? extends JsonSerializer>[] serClass() default {};

    enum Type {
        GID,
        PHONE,
        DEFAULT
    }

}
