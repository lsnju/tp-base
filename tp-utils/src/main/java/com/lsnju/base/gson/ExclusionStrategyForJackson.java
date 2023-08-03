package com.lsnju.base.gson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 *
 * @author ls
 * @since 2021/3/25 21:43
 * @version V1.0
 */
public class ExclusionStrategyForJackson implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        JsonIgnore jsonIgnore = f.getAnnotation(JsonIgnore.class);
        return jsonIgnore != null && jsonIgnore.value();
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        JsonIgnore jsonIgnore = clazz.getAnnotation(JsonIgnore.class);
        return jsonIgnore != null && jsonIgnore.value();
    }
}
