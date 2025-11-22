package com.lsnju.base.jackson;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.dataformat.xml.XmlMapper;

/**
 *
 * @author ls
 * @since 2021/1/10 10:49
 * @version V1.0
 */
public class JacksonXmlUtils {

    private static final XmlMapper MAPPER = XmlMapper.builder()
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();

    public static String toXml(Object obj) {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T fromXml(String xml, Class<T> clazz) {
        return MAPPER.readValue(xml, clazz);
    }

    public static <T> T fromXml(String xml, TypeReference<T> type) {
        return MAPPER.readValue(xml, type);
    }

}
