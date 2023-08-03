package com.lsnju.base.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 *
 * @author ls
 * @since 2021/1/10 10:49
 * @version V1.0
 */
public class JacksonXmlUtils {

    private static final XmlMapper MAPPER = new XmlMapper();

    static {
        MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public static String toXml(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T fromXml(String xml, Class<T> clazz) throws IOException {
        return MAPPER.readValue(xml, clazz);
    }

    public static <T> T fromXml(String xml, TypeReference<T> type) throws JsonProcessingException {
        return MAPPER.readValue(xml, type);
    }

}
