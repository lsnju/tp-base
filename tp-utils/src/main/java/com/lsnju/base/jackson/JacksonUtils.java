package com.lsnju.base.jackson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.lsnju.base.money.Money;
import com.lsnju.base.util.ClazzUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lisong
 * @since 2020/3/18 19:23
 * @version V1.0
 */
@Slf4j
public class JacksonUtils {

    public static final JsonMapper DEFAULT_MAPPER;
    public static final JsonMapper PRETTY_MAPPER;
    public static boolean WITH_JSR310 = ClazzUtils.exist("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
    public static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    static {
        StdDateFormat dateFormat = new StdDateFormat()
            .withColonInTimeZone(true)
            .withLenient(true)
            .withTimeZone(TimeZone.getDefault());

        DEFAULT_MAPPER = JsonMapper.builder()
            // write
            .defaultPropertyInclusion(JsonInclude.Value.ALL_NON_NULL)
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL)
            .enable(JsonWriteFeature.WRITE_HEX_UPPER_CASE)
            .enable(StreamWriteFeature.IGNORE_UNKNOWN)
            // read
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
            // add module
            .addModule(getDefaultModule())
            .defaultDateFormat(dateFormat)
            .build();

        PRETTY_MAPPER = DEFAULT_MAPPER.rebuild()
            // write
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();
    }

    public static SimpleModule getDefaultModule() {
        if (WITH_JSR310) {
            return getJavaTimeModule();
        }
        return getSimpleModule();
    }

    public static SimpleModule getJavaTimeModule() {
        return JacksonJsr310Utils.JAVA_TIME_MODULE;
    }

    public static SimpleModule getSimpleModule() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(Money.class, new MoneySerializer());
        module.addSerializer(ZonedDateTime.class, new TpZonedDateTimeSerializer());
        module.addSerializer(LocalDateTime.class, new TpLocalDateTimeSerializer());
        module.addSerializer(LocalDate.class, new TpLocalDateSerializer());
        module.addSerializer(LocalTime.class, new TpLocalTimeSerializer());

        module.addDeserializer(Money.class, new MoneyDeserializer());
        module.addDeserializer(ZonedDateTime.class, new TpZonedDateTimeDeserializer());
        module.addDeserializer(LocalDateTime.class, new TpLocalDateTimeDeserializer());
        module.addDeserializer(LocalDate.class, new TpLocalDateDeserializer());
        module.addDeserializer(LocalTime.class, new TpLocalTimeDeserializer());
        return module;
    }

    public static Map<String, String> toMap(Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String json) {
            if (StringUtils.isBlank(json)) {
                return null;
            }
            return DEFAULT_MAPPER.readValue(json, MAP_TYPE_REFERENCE);
        }
        return DEFAULT_MAPPER.convertValue(obj, MAP_TYPE_REFERENCE);
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

    public static String toJsonPretty(Object obj) {
        if (obj == null) {
            return null;
        }
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
        try (final JsonParser jp = DEFAULT_MAPPER.getFactory().createParser(json)) {
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
                                    final long begin = jp.currentLocation().getCharOffset();
                                    jp.skipChildren();
                                    final long end = jp.currentLocation().getCharOffset();
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
        }
        return StringUtils.EMPTY;
    }
}
