package com.lsnju.base.gson;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.FieldNamingStrategy;

/**
 *
 * @author ls
 * @since 2021/3/25 21:40
 * @version V1.0
 */
public class FieldNamingStrategyForJackson implements FieldNamingStrategy {
    @Override
    public String translateName(Field f) {
        final JsonProperty jsonProperty = f.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        return f.getName();
    }
}
