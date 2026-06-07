package com.lsnju.base.gson.mask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lsnju.base.gson.FieldNamingStrategyForJackson;
import com.lsnju.base.gson.GsonUtils;
import com.lsnju.base.jackson.annotation.Mask;

/**
 * Gson adapter factory that masks string fields annotated with {@link Mask}.
 *
 * @author ls
 */
public class MaskAnnotationTypeAdapterFactory implements TypeAdapterFactory {

    private static final FieldNamingStrategyForJackson JACKSON_NAMING = new FieldNamingStrategyForJackson();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<?> rawType = type.getRawType();
        if (rawType.isPrimitive() || rawType.isEnum() || rawType == String.class || Number.class.isAssignableFrom(rawType)
            || Boolean.class == rawType || Character.class == rawType) {
            return null;
        }
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return (TypeAdapter<T>) new TypeAdapter<Object>() {
            @Override
            public void write(JsonWriter out, Object value) throws IOException {
                JsonElement json = delegate.toJsonTree((T) value);
                JsonElement masked = maskElement(value, json, new IdentityHashMap<>());
                gson.toJson(masked, out);
            }

            @Override
            public Object read(JsonReader in) throws IOException {
                return delegate.read(in);
            }
        }.nullSafe();
    }

    private JsonElement maskElement(Object source, JsonElement element, IdentityHashMap<Object, Boolean> visited) {
        if (source == null || element == null || element.isJsonNull()) {
            return element;
        }
        if (visited.put(source, Boolean.TRUE) != null) {
            return element;
        }
        try {
            if (element.isJsonObject()) {
                if (source instanceof Map) {
                    maskJsonObjectFromMap((Map<?, ?>) source, element.getAsJsonObject(), visited);
                    return element;
                }
                maskJsonObject(source, element.getAsJsonObject(), visited);
            } else if (element.isJsonArray()) {
                maskJsonArray(source, element.getAsJsonArray(), visited);
            }
            return element;
        } finally {
            visited.remove(source);
        }
    }

    private void maskJsonObjectFromMap(Map<?, ?> source, JsonObject jsonObject, IdentityHashMap<Object, Boolean> visited) {
        for (Map.Entry<?, ?> entry : source.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                continue;
            }
            String key = (String) entry.getKey();
            if (!jsonObject.has(key)) {
                continue;
            }
            Object value = entry.getValue();
            if (value != null) {
                maskElement(value, jsonObject.get(key), visited);
            }
        }
    }

    private void maskJsonObject(Object source, JsonObject jsonObject, IdentityHashMap<Object, Boolean> visited) {
        for (Field field : getAllFields(source.getClass())) {
            if (shouldSkip(field)) {
                continue;
            }
            Set<String> names = resolveJsonFieldNames(field);
            if (names.isEmpty()) {
                continue;
            }
            field.setAccessible(true);
            Object fieldValue;
            try {
                fieldValue = field.get(source);
            } catch (IllegalAccessException ignore) {
                continue;
            }
            Mask annotation = field.getAnnotation(Mask.class);
            for (String name : names) {
                if (!jsonObject.has(name)) {
                    continue;
                }
                JsonElement current = jsonObject.get(name);
                if (annotation != null && isStringPrimitive(current)) {
                    String raw = current.getAsString();
                    jsonObject.addProperty(name, MaskingRuleResolver.mask(annotation, raw));
                    continue;
                }
                if (fieldValue != null) {
                    maskElement(fieldValue, current, visited);
                }
            }
        }
    }

    private void maskJsonArray(Object source, JsonArray array, IdentityHashMap<Object, Boolean> visited) {
        if (source.getClass().isArray()) {
            int len = Array.getLength(source);
            for (int i = 0; i < len && i < array.size(); i++) {
                Object item = Array.get(source, i);
                if (item != null) {
                    maskElement(item, array.get(i), visited);
                }
            }
            return;
        }
        if (source instanceof Iterable) {
            Iterator<?> it = ((Iterable<?>) source).iterator();
            for (int i = 0; i < array.size() && it.hasNext(); i++) {
                Object item = it.next();
                if (item != null) {
                    maskElement(item, array.get(i), visited);
                }
            }
            return;
        }
        if (source instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) source;
            for (int i = 0; i < array.size(); i++) {
                JsonElement item = array.get(i);
                if (!item.isJsonObject()) {
                    continue;
                }
                // For map serialized as array (rare custom adapters), recurse best-effort.
                maskElement(map, item, visited);
            }
        }
    }

    private boolean isStringPrimitive(JsonElement element) {
        if (!(element instanceof JsonPrimitive)) {
            return false;
        }
        JsonPrimitive primitive = (JsonPrimitive) element;
        return primitive.isString();
    }

    private Set<String> resolveJsonFieldNames(Field field) {
        Set<String> names = new LinkedHashSet<>();
        SerializedName serializedName = field.getAnnotation(SerializedName.class);
        if (serializedName != null) {
            names.add(serializedName.value());
            Collections.addAll(names, serializedName.alternate());
            return names;
        }
        if (GsonUtils.WITH_JACKSON) {
            names.add(JACKSON_NAMING.translateName(field));
            return names;
        }
        names.add(field.getName());
        return names;
    }

    private boolean shouldSkip(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || field.isSynthetic();
    }

    private Field[] getAllFields(Class<?> type) {
        if (type == null || Object.class == type) {
            return new Field[0];
        }
        Field[] current = type.getDeclaredFields();
        Field[] parent = getAllFields(type.getSuperclass());
        Field[] merged = new Field[current.length + parent.length];
        System.arraycopy(current, 0, merged, 0, current.length);
        System.arraycopy(parent, 0, merged, current.length, parent.length);
        return merged;
    }
}
