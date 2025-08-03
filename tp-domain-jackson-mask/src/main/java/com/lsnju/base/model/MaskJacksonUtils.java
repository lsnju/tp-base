package com.lsnju.base.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsnju.base.jackson.JacksonUtils;
import com.lsnju.base.jackson.mask.MaskAnnotationIntrospector;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/7/22 21:31
 * @version V1.0
 */
@Slf4j
public class MaskJacksonUtils {

    private static final ObjectMapper DEFAULT_MAPPER = JacksonUtils.DEFAULT_MAPPER.copy();

    static {
        DEFAULT_MAPPER.setAnnotationIntrospector(new MaskAnnotationIntrospector());
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

}
