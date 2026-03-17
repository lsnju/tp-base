package com.lsnju.base.model;

import com.lsnju.base.jackson.JacksonUtils;
import com.lsnju.base.jackson.mask.MaskAnnotationIntrospector;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author lis614
 * @since 2025/7/22 21:31
 * @version V1.0
 */
@Slf4j
public class MaskJacksonUtils {

    private static final ObjectMapper DEFAULT_MAPPER;

    static {
        DEFAULT_MAPPER = JacksonUtils.DEFAULT_MAPPER.rebuild()
            .annotationIntrospector(new MaskAnnotationIntrospector())
            .build();
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return DEFAULT_MAPPER.writeValueAsString(obj);
    }

}
