package com.lsnju.base.util;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author lisong
 * @since 2022/9/1 14:15
 * @version V1.0
 */
public class TpJsonUtils {

    public static Map<String, String> toMap(Object obj) {
        return DEFAULT.toMap(obj);
    }

    public static String toJson(Object obj) {
        return DEFAULT.toJson(obj);
    }

    public static String toJsonPretty(Object obj) {
        return DEFAULT.toJsonPretty(obj);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return DEFAULT.fromJson(jsonStr, clazz);
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        return DEFAULT.fromJson(jsonStr, type);
    }

    public static <T> T fromMap(Map<?, ?> map, Class<T> clazz) {
        return DEFAULT.fromMap(map, clazz);
    }

    public static <T> T fromMap(Map<?, ?> map, Type type) {
        return DEFAULT.fromMap(map, type);
    }

    public static String toPrettyFormat(String jsonString) {
        return DEFAULT.toPrettyFormat(jsonString);
    }

    public static boolean isValidJson(String jsonString) {
        return DEFAULT.isValidJson(jsonString);
    }

    public static String getRawValue(final String jsonString, final String tag) {
        return DEFAULT.getRawValue(jsonString, tag);
    }

    public static final TpJson GSON = TpJsonFactory.getGson();
    public static final TpJson JACKSON = TpJsonFactory.getJackson();
    public static final TpJson DEFAULT = TpJsonFactory.getDefault();

    public static TpJson gson() {
        return Objects.requireNonNull(TpJsonFactory.getGson());
    }

    public static TpJson jackson() {
        return Objects.requireNonNull(TpJsonFactory.getJackson());
    }

    public interface TpJson {

        Map<String, String> toMap(Object obj);

        String toJson(Object obj);

        String toJsonPretty(Object obj);

        <T> T fromJson(String jsonStr, Class<T> clazz);

        <T> T fromJson(String jsonStr, Type type);

        <T> T fromMap(Map<?, ?> map, Class<T> clazz);

        <T> T fromMap(Map<?, ?> map, Type type);

        String toPrettyFormat(String jsonString);

        boolean isValidJson(String jsonString);

        String getRawValue(final String jsonString, final String tag);

    }

}
