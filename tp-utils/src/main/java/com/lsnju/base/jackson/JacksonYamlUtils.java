package com.lsnju.base.jackson;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ls
 * @since 2020/5/12 15:04
 * @version V1.0
 */
@Slf4j
public class JacksonYamlUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    public static String toYml(Object obj) {
        Objects.requireNonNull(obj);
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    public static <T> T fromYml(String ymlStr, Class<T> clazz) {
        if (StringUtils.isBlank(ymlStr)) {
            return null;
        }
        Objects.requireNonNull(clazz);
        try {
            return MAPPER.readValue(ymlStr, clazz);
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }
}
