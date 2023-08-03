package com.lsnju.base.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.lsnju.base.gson.GsonUtils;
import com.lsnju.base.jackson.MoneyDeserializer;
import com.lsnju.base.jackson.MoneySerializer;
import com.lsnju.base.money.Money;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2022/9/1 14:21
 * @version V1.0
 */
@Slf4j
class TpJsonFactory {

    static boolean WITH_JACKSON = ClazzUtils.exist("com.fasterxml.jackson.databind.ObjectMapper");
    static boolean WITH_GSON = ClazzUtils.exist("com.google.gson.Gson");
    private static TpJsonUtils.TpJson GSON;
    private static TpJsonUtils.TpJson JACKSON;

    static {
        if (WITH_GSON) {
            try {
                GSON = new GsonTpJson();
            } catch (Exception ignore) {
            }
        }
        if (WITH_JACKSON) {
            try {
                JACKSON = new JacksonTpJson();
            } catch (Exception ignore) {
            }
        }
    }

    static TpJsonUtils.TpJson getDefault() {
        if (GSON != null) {
            return GSON;
        } else if (JACKSON != null) {
            return JACKSON;
        }
        throw new RuntimeException("no gson or jackson lib exist.");
    }

    static TpJsonUtils.TpJson getGson() {
        return GSON;
    }

    static TpJsonUtils.TpJson getJackson() {
        return JACKSON;
    }

    static class GsonTpJson implements TpJsonUtils.TpJson {

        @Override
        public Map<String, String> toMap(Object obj) {
            return GsonUtils.toMap(obj);
        }

        @Override
        public String toJson(Object obj) {
            return GsonUtils.toJson(obj);
        }

        @Override
        public String toJsonPretty(Object obj) {
            return GsonUtils.toJsonPretty(obj);
        }

        @Override
        public <T> T fromJson(String jsonStr, Class<T> clazz) {
            return GsonUtils.fromJson(jsonStr, clazz);
        }

        @Override
        public <T> T fromJson(String jsonStr, Type type) {
            return GsonUtils.fromJson(jsonStr, type);
        }

        @Override
        public <T> T fromMap(Map<?, ?> map, Class<T> clazz) {
            return GsonUtils.fromMap(map, clazz);
        }

        @Override
        public <T> T fromMap(Map<?, ?> map, Type type) {
            return GsonUtils.fromMap(map, type);
        }

        @Override
        public String toPrettyFormat(String jsonString) {
            return GsonUtils.toPrettyFormat(jsonString);
        }

        @Override
        public boolean isValidJson(String jsonString) {
            return GsonUtils.isValidJson(jsonString);
        }

        @Override
        public String getRawValue(String jsonString, String tag) {
            return GsonUtils.getRawValue(jsonString, tag);
        }
    }

    static class JacksonTpJson implements TpJsonUtils.TpJson {

        public static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();
        public static final ObjectMapper PRETTY_MAPPER = new ObjectMapper();
        public static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, String>>() {};

        static {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PRETTY_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
            PRETTY_MAPPER.setDateFormat(dateFormat);
            DEFAULT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DEFAULT_MAPPER.setDateFormat(dateFormat);

            SimpleModule module = new SimpleModule();
            module.addSerializer(Money.class, new MoneySerializer());
            module.addDeserializer(Money.class, new MoneyDeserializer());
            DEFAULT_MAPPER.registerModule(module);
            PRETTY_MAPPER.registerModule(module);
            // MAPPER.registerModule(new JaxbAnnotationModule());
        }

        @Override
        public Map<String, String> toMap(Object obj) throws IOException {
            if (obj instanceof String) {
                return DEFAULT_MAPPER.readValue((String) obj, MAP_TYPE_REFERENCE);
            }
            return DEFAULT_MAPPER.convertValue(obj, MAP_TYPE_REFERENCE);
        }

        @Override
        public String toJson(Object obj) throws IOException {
            Objects.requireNonNull(obj);
            return DEFAULT_MAPPER.writeValueAsString(obj);
        }

        @Override
        public String toJsonPretty(Object obj) throws IOException {
            Objects.requireNonNull(obj);
            return PRETTY_MAPPER.writeValueAsString(obj);
        }

        @Override
        public <T> T fromJson(String jsonStr, Class<T> clazz) throws IOException {
            if (StringUtils.isBlank(jsonStr)) {
                return null;
            }
            Objects.requireNonNull(clazz);
            return DEFAULT_MAPPER.readValue(jsonStr, clazz);
        }

        @Override
        public <T> T fromJson(String jsonStr, Type type) throws IOException {
            if (StringUtils.isBlank(jsonStr)) {
                return null;
            }
            Objects.requireNonNull(type);
            return DEFAULT_MAPPER.readValue(jsonStr, DEFAULT_MAPPER.constructType(type));
        }

        @Override
        public <T> T fromMap(Map<?, ?> map, Class<T> clazz) {
            return DEFAULT_MAPPER.convertValue(map, clazz);
        }

        @Override
        public <T> T fromMap(Map<?, ?> map, Type type) {
            return DEFAULT_MAPPER.convertValue(map, DEFAULT_MAPPER.constructType(type));
        }

        @Override
        public String toPrettyFormat(String jsonString) throws IOException {
            JsonNode root = DEFAULT_MAPPER.readTree(jsonString);
            if (root == null) {
                return jsonString;
            }
            return toJsonPretty(root);
        }

        @Override
        public boolean isValidJson(String jsonString) {
            try {
                JsonNode root = DEFAULT_MAPPER.readTree(jsonString);
                return root != null;
            } catch (JsonProcessingException e) {
                log.info("{}", e.getMessage());
                return false;
            }
        }

        @Override
        public String getRawValue(String jsonString, String tag) {
            return GsonUtils.getRawValue(jsonString, tag);
        }

    }
}
