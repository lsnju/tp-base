package com.lsnju.base.jackson;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.dataformat.yaml.YAMLMapper;

/**
 * @author ls
 * @since 2020/5/12 15:04
 * @version V1.0
 */
@Slf4j
public class JacksonYamlUtils {

    private static final YAMLMapper MAPPER = YAMLMapper.builder()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build();

    public static String toYml(Object obj) {
        Objects.requireNonNull(obj);
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T fromYml(String ymlStr, Class<T> clazz) {
        if (StringUtils.isBlank(ymlStr)) {
            return null;
        }
        Objects.requireNonNull(clazz);
        return MAPPER.readValue(ymlStr, clazz);
    }

}
