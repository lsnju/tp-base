package com.lsnju.base.jackson;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lsnju.base.money.Money;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.core.json.JsonWriteFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.util.StdDateFormat;

/**
 * @author lisong
 * @since 2020/3/18 19:23
 * @version V1.0
 */
@Slf4j
public class JacksonUtils {

    public static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE = new TypeReference<>() {};
    public static final JsonMapper DEFAULT_MAPPER;
    public static final JsonMapper PRETTY_MAPPER;

    static {
        StdDateFormat dateFormat = new StdDateFormat()
            .withColonInTimeZone(true)
            .withLenient(true)
            .withTimeZone(TimeZone.getDefault());

        DEFAULT_MAPPER = JsonMapper.builder()
            // write
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL)
            .enable(JsonWriteFeature.WRITE_HEX_UPPER_CASE)
            .enable(StreamWriteFeature.IGNORE_UNKNOWN)
            // read
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
            // add module
            .addModule(getDefaultModule())
            .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
            .defaultDateFormat(dateFormat)
            .build();

        PRETTY_MAPPER = DEFAULT_MAPPER.rebuild()
            // write
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();
    }

    public static SimpleModule getDefaultModule() {
        return getSimpleModule();
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

    public static Map<String, String> toMap(Object obj) {
        if (obj instanceof String) {
            return DEFAULT_MAPPER.readValue((String) obj, MAP_TYPE_REFERENCE);
        }
        return DEFAULT_MAPPER.convertValue(obj, MAP_TYPE_REFERENCE);
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return DEFAULT_MAPPER.writeValueAsString(obj);
    }

    public static String toJsonPretty(Object obj) {
        if (obj == null) {
            return null;
        }
        return PRETTY_MAPPER.writeValueAsString(obj);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Objects.requireNonNull(clazz);
        return DEFAULT_MAPPER.readValue(jsonStr, clazz);
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> type) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Objects.requireNonNull(type);
        return DEFAULT_MAPPER.readValue(jsonStr, type);
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Objects.requireNonNull(type);
        return DEFAULT_MAPPER.readValue(jsonStr, DEFAULT_MAPPER.constructType(type));
    }

}
