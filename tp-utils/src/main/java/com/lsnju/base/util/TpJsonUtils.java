package com.lsnju.base.util;

import java.io.IOException;
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

    public static Map<String, String> toMap(Object obj) throws IOException {
        return DEFAULT.toMap(obj);
    }

    public static String toJson(Object obj) throws IOException {
        return DEFAULT.toJson(obj);
    }

    public static String toJsonPretty(Object obj) throws IOException {
        return DEFAULT.toJsonPretty(obj);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) throws IOException {
        return DEFAULT.fromJson(jsonStr, clazz);
    }

    public static <T> T fromJson(String jsonStr, Type type) throws IOException {
        return DEFAULT.fromJson(jsonStr, type);
    }

    public static <T> T fromMap(Map<?, ?> map, Class<T> clazz) {
        return DEFAULT.fromMap(map, clazz);
    }

    public static <T> T fromMap(Map<?, ?> map, Type type) {
        return DEFAULT.fromMap(map, type);
    }

    public static String toPrettyFormat(String jsonString) throws IOException {
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

        Map<String, String> toMap(Object obj) throws IOException;

        String toJson(Object obj) throws IOException;

        String toJsonPretty(Object obj) throws IOException;

        <T> T fromJson(String jsonStr, Class<T> clazz) throws IOException;

        <T> T fromJson(String jsonStr, Type type) throws IOException;

        <T> T fromMap(Map<?, ?> map, Class<T> clazz);

        <T> T fromMap(Map<?, ?> map, Type type);

        String toPrettyFormat(String jsonString) throws IOException;

        boolean isValidJson(String jsonString);

        String getRawValue(final String jsonString, final String tag);

    }

}
