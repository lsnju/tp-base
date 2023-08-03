package com.lsnju.base.gson;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lsnju.base.money.Money;
import com.lsnju.base.util.ClazzUtils;

/**
 * @author lisong
 * @since 2020/3/19 8:17
 * @version V1.0
 */
public class GsonUtils {

    public static final int MAX_SIZE = 1 << 13;
    public static final Type MAP_TYPE_REFERENCE = new TypeToken<Map<String, String>>() {}.getType();
    public static boolean WITH_JACKSON = ClazzUtils.exist("com.fasterxml.jackson.annotation.JsonProperty");
    private static final Gson GSON = build(false);
    private static final Gson GP = build(true);

    public static Gson build(boolean pretty) {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        builder.registerTypeAdapter(Money.class, new MoneyAdapter());
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        if (WITH_JACKSON) {
            builder.setFieldNamingStrategy(new FieldNamingStrategyForJackson());
            builder.setExclusionStrategies(new ExclusionStrategyForJackson());
        }
        if (pretty) {
            builder.setPrettyPrinting();
        }
        return builder.create();
    }

    public static Map<String, String> toMap(Object obj) {
        Objects.requireNonNull(obj);
        if (obj instanceof String) {
            return fromJson((String) obj, MAP_TYPE_REFERENCE);
        }
        return GSON.fromJson(GSON.toJsonTree(obj), MAP_TYPE_REFERENCE);
    }

    public static String toJson(Object obj) {
        Objects.requireNonNull(obj);
        return GSON.toJson(obj);
    }

    public static String toJsonPretty(Object obj) {
        Objects.requireNonNull(obj);
        return GP.toJson(obj);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return GSON.fromJson(jsonStr, clazz);
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        return GSON.fromJson(jsonStr, type);
    }

    public static <T> T fromMap(Map<?, ?> map, Class<T> clazz) {
        JsonElement jsonElement = GSON.toJsonTree(map);
        return GSON.fromJson(jsonElement, clazz);
    }

    public static <T> T fromMap(Map<?, ?> map, Type type) {
        JsonElement jsonElement = GSON.toJsonTree(map);
        return GSON.fromJson(jsonElement, type);
    }

    public static String toPrettyFormat(String jsonString) {
        final JsonElement jsonElement = JsonParser.parseString(jsonString);
        if (jsonElement == null) {
            return jsonString;
        }
        if (jsonElement.isJsonArray()) {
            return toJsonPretty(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonObject()) {
            return toJsonPretty(jsonElement.getAsJsonObject());
        }
        return jsonString;
    }

    public static boolean isValidJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            return JsonParser.parseString(json) != null;
        } catch (JsonSyntaxException ignore) {
            return false;
        }
    }

    public static String getRawValue(final String json, final String tag) {
        String aTag = String.format("\"%s\":", tag);
        int index = StringUtils.indexOf(json, aTag);
        if (index < 0) {
            return StringUtils.EMPTY;
        }
        for (int i = index + aTag.length(); i < MAX_SIZE; i++) {
            switch (json.charAt(i)) {
                case ' ':
                    continue;
                case '{':
                    return getObjectValue(i, json);
                case '"':
                    return getStringValue(i, json);
                case '[':
                    return getArrayValue(i, json);
                default:
                    return getValue(i, json);
            }
        }
        return StringUtils.EMPTY;
    }

    private static String getValue(final int fromIndex, final String json) {
        final StringBuilder sb = new StringBuilder();
        for (int i = fromIndex, max = json.length(); i < max; i++) {
            char c = json.charAt(i);
            if (c == ',' || c == '}') {
                String retValue = sb.toString();
                if (StringUtils.equals(retValue, "null")) {
                    return null;
                }
                return retValue;
            }
            sb.append(c);
        }
        return StringUtils.EMPTY;
    }

    private static String getObjectValue(final int fromIndex, final String json) {
        final StringBuilder sb = new StringBuilder();
        boolean escapes = false;
        boolean strData = false;
        for (int count = 0, i = fromIndex, max = json.length(); i < max; i++) {
            final char c = json.charAt(i);
            if (escapes) {
                sb.append('\\').append(c);
                escapes = false;
            } else {
                switch (c) {
                    case '"':
                        sb.append(c);
                        strData = !strData;
                        break;
                    case '\\':
                        escapes = true;
                        break;
                    case '{':
                        sb.append(c);
                        if (!strData) {
                            count++;
                        }
                        break;
                    case '}':
                        sb.append(c);
                        if (!strData) {
                            if (--count == 0) {
                                return sb.toString();
                            }
                        }
                        break;
                    default:
                        sb.append(c);
                }
            }
        }
        return StringUtils.EMPTY;
    }

    private static String getArrayValue(final int fromIndex, final String json) {
        final StringBuilder sb = new StringBuilder();
        boolean escapes = false;
        boolean strData = false;
        for (int count = 0, i = fromIndex, max = json.length(); i < max; i++) {
            final char c = json.charAt(i);
            if (escapes) {
                sb.append('\\').append(c);
                escapes = false;
            } else {
                switch (c) {
                    case '"':
                        sb.append(c);
                        strData = !strData;
                        break;
                    case '\\':
                        escapes = true;
                        break;
                    case '[':
                        sb.append(c);
                        if (!strData) {
                            count++;
                        }
                        break;
                    case ']':
                        sb.append(c);
                        if (!strData) {
                            if (--count == 0) {
                                return sb.toString();
                            }
                        }
                        break;
                    default:
                        sb.append(c);
                }
            }
        }
        return StringUtils.EMPTY;
    }

    private static String getStringValue(final int fromIndex, final String json) {
        final StringBuilder sb = new StringBuilder();
        boolean escapes = false;
        for (int i = fromIndex + 1, max = json.length(); i < max; i++) {
            char c = json.charAt(i);
            if (escapes) {
                sb.append('\\').append(c);
                escapes = false;
            } else {
                switch (c) {
                    case '"':
                        return sb.toString();
                    case '\\':
                        escapes = true;
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
        }
        return StringUtils.EMPTY;
    }
}
