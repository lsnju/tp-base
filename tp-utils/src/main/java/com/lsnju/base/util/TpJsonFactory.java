package com.lsnju.base.util;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.lsnju.base.gson.GsonUtils;
import com.lsnju.base.jackson.JacksonUtils;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 *
 * @author lisong
 * @since 2022/9/1 14:21
 * @version V1.0
 */
@Slf4j
class TpJsonFactory {

    static boolean WITH_JACKSON = ClazzUtils.exist("tools.jackson.databind.json.JsonMapper");
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

        public static final JsonMapper DEFAULT_MAPPER;
        public static final JsonMapper PRETTY_MAPPER;
        public static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

        static {
            DEFAULT_MAPPER = JacksonUtils.DEFAULT_MAPPER.rebuild()
                .build();
            PRETTY_MAPPER = JacksonUtils.PRETTY_MAPPER.rebuild()
                .build();
        }

        @Override
        public Map<String, String> toMap(Object obj) {
            if (obj instanceof String) {
                return DEFAULT_MAPPER.readValue((String) obj, MAP_TYPE_REFERENCE);
            }
            return DEFAULT_MAPPER.convertValue(obj, MAP_TYPE_REFERENCE);
        }

        @Override
        public String toJson(Object obj) {
            Objects.requireNonNull(obj);
            return DEFAULT_MAPPER.writeValueAsString(obj);
        }

        @Override
        public String toJsonPretty(Object obj) {
            Objects.requireNonNull(obj);
            return PRETTY_MAPPER.writeValueAsString(obj);
        }

        @Override
        public <T> T fromJson(String jsonStr, Class<T> clazz) {
            if (StringUtils.isBlank(jsonStr)) {
                return null;
            }
            Objects.requireNonNull(clazz);
            return DEFAULT_MAPPER.readValue(jsonStr, clazz);
        }

        @Override
        public <T> T fromJson(String jsonStr, Type type) {
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
        public String toPrettyFormat(String jsonString) {
            JsonNode root = DEFAULT_MAPPER.readTree(jsonString);
            if (root == null) {
                return jsonString;
            }
            return toJsonPretty(root);
        }

        @Override
        public boolean isValidJson(String jsonString) {
            JsonNode root = DEFAULT_MAPPER.readTree(jsonString);
            return root != null;
        }

        @Override
        public String getRawValue(String jsonString, String tag) {
            return GsonUtils.getRawValue(jsonString, tag);
        }

    }
}
