package com.lsnju.base.jackson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.lsnju.base.money.Money;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lisong
 * @since 2020/3/18 19:23
 * @version V1.0
 */
@Slf4j
public class JacksonUtils {

    public static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();
    public static final ObjectMapper PRETTY_MAPPER = new ObjectMapper();
    public static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, String>>() {};

    static {
        PRETTY_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        DEFAULT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Money.class, new MoneySerializer());
        module.addDeserializer(Money.class, new MoneyDeserializer());
        DEFAULT_MAPPER.registerModule(module);
        PRETTY_MAPPER.registerModule(module);
        // MAPPER.registerModule(new JaxbAnnotationModule());
    }

    public static Map<String, String> toMap(Object obj) throws IOException {
        if (obj instanceof String) {
            return DEFAULT_MAPPER.readValue((String) obj, MAP_TYPE_REFERENCE);
        }
        return DEFAULT_MAPPER.convertValue(obj, MAP_TYPE_REFERENCE);
    }

    public static String toJson(Object obj) {
        Objects.requireNonNull(obj);
        try {
            return DEFAULT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    public static String toJsonPretty(Object obj) {
        Objects.requireNonNull(obj);
        try {
            return PRETTY_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Objects.requireNonNull(clazz);
        try {
            return DEFAULT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> type) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Objects.requireNonNull(type);
        try {
            return DEFAULT_MAPPER.readValue(jsonStr, type);
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Objects.requireNonNull(type);
        try {
            return DEFAULT_MAPPER.readValue(jsonStr, DEFAULT_MAPPER.constructType(type));
        } catch (JsonProcessingException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    public static String getRawValue(String json, String[] path) throws IOException {
        final JsonParser jp = DEFAULT_MAPPER.getFactory().createParser(json);
        for (int i = 0, max = path.length; i < max; i++) {
            log.debug("current path = {}", path[i]);
            if (jp.nextToken() == JsonToken.START_OBJECT) {
                boolean found = false;
                for (String fieldName = jp.nextFieldName(); fieldName != null; fieldName = jp.nextFieldName()) {
                    log.debug("current fieldName = {}", fieldName);
                    if (fieldName.equals(path[i])) {
                        if (i == max - 1) {
                            final JsonToken jsonToken = jp.nextToken();
                            log.debug("nextToken = {}", jsonToken);
                            if (jsonToken == JsonToken.START_OBJECT || jsonToken == JsonToken.START_ARRAY) {
                                final long begin = jp.getCurrentLocation().getCharOffset();
                                jp.skipChildren();
                                final long end = jp.getCurrentLocation().getCharOffset();
                                log.debug("{} - {}", begin, end);
                                return json.substring((int) begin - 1, (int) end);
                            } else if (jsonToken == JsonToken.VALUE_STRING) {
                                return jp.getText();
                            } else if (jsonToken == JsonToken.VALUE_NUMBER_INT
                                || jsonToken == JsonToken.VALUE_NUMBER_FLOAT
                                || jsonToken == JsonToken.VALUE_FALSE
                                || jsonToken == JsonToken.VALUE_TRUE
                                || jsonToken == JsonToken.VALUE_NULL) {
                                return jp.getValueAsString();
                            }
                            throw new NotImplementedException();
                        }
                        found = true;
                        break;
                    } else {
                        switch (jp.nextToken()) {
                            case START_OBJECT:
                            case START_ARRAY:
                                jp.skipChildren();
                                break;
                            case VALUE_STRING:
                                jp.finishToken();
                                break;
                            default:
                        }
                    }
                }
                if (!found) {
                    return StringUtils.EMPTY;
                }
            } else {
                return StringUtils.EMPTY;
            }
        }
        return StringUtils.EMPTY;
    }
}
