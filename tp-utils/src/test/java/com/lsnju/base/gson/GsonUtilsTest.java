package com.lsnju.base.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lsnju.base.money.Money;

/**
 * Unit tests for {@link GsonUtils}.
 *
 * @author ls
 */
class GsonUtilsTest {

    static class Person {
        String name;
        int age;
        LocalDateTime at;

        Person() {}

        Person(String name, int age, LocalDateTime at) {
            this.name = name;
            this.age = age;
            this.at = at;
        }
    }

    @Test
    void build_pretty_hasNewlines() {
        Person p = new Person("a", 1, LocalDateTime.of(2024, 6, 1, 12, 0));
        String compact = GsonUtils.build(false).toJson(p);
        String pretty = GsonUtils.build(true).toJson(p);
        assertFalse(compact.contains("\n"));
        assertTrue(pretty.contains("\n"));
    }

    @Test
    void toJson_fromJson_roundTrip() {
        LocalDateTime at = LocalDateTime.of(2024, 6, 1, 12, 30, 45);
        Person original = new Person("bob", 30, at);
        String json = GsonUtils.toJson(original);
        Person back = GsonUtils.fromJson(json, Person.class);
        assertEquals(original.name, back.name);
        assertEquals(original.age, back.age);
        assertEquals(original.at, back.at);
    }

    @Test
    void toJsonPretty_notNull() {
        Map<String, String> m = new HashMap<>();
        m.put("k", "v");
        assertTrue(GsonUtils.toJsonPretty(m).contains("\n"));
    }

    @Test
    void toJson_null_throws() {
        assertThrows(NullPointerException.class, () -> GsonUtils.toJson(null));
    }

    @Test
    void toJsonPretty_null_throws() {
        assertThrows(NullPointerException.class, () -> GsonUtils.toJsonPretty(null));
    }

    @Test
    void fromJson_invalid_throws() {
        assertThrows(JsonSyntaxException.class, () -> GsonUtils.fromJson("{", Person.class));
    }

    @Test
    void fromJson_withType_tokenList() {
        String json = "[\"a\",\"b\"]";
        List<String> list = GsonUtils.fromJson(json, new TypeToken<List<String>>() {}.getType());
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    void money_roundTrip_viaGsonUtils() {
        Money m = new Money("12.34");
        String json = GsonUtils.toJson(m);
        Money back = GsonUtils.fromJson(json, Money.class);
        assertEquals(m, back);
    }

    @Test
    void toMap_fromObject() {
        Map<String, String> in = new HashMap<>();
        in.put("x", "1");
        in.put("y", "2");
        Map<String, String> map = GsonUtils.toMap(in);
        assertEquals("1", map.get("x"));
        assertEquals("2", map.get("y"));
    }

    @Test
    void toMap_fromJsonString() {
        Map<String, String> map = GsonUtils.toMap("{\"a\":\"b\",\"c\":\"d\"}");
        assertEquals("b", map.get("a"));
        assertEquals("d", map.get("c"));
    }

    @Test
    void toMap_null_throws() {
        assertThrows(NullPointerException.class, () -> GsonUtils.toMap(null));
    }

    @Test
    void fromMap_toClass() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "n");
        map.put("age", 7.0);
        map.put("at", "2024-01-02T03:04:05");
        Person p = GsonUtils.fromMap(map, Person.class);
        assertEquals("n", p.name);
        assertEquals(7, p.age);
        assertEquals(LocalDateTime.of(2024, 1, 2, 3, 4, 5), p.at);
    }

    @Test
    void fromMap_toType() {
        Map<String, String> map = new HashMap<>();
        map.put("k", "v");
        Map<String, String> out = GsonUtils.fromMap(map, GsonUtils.MAP_TYPE_REFERENCE);
        assertEquals("v", out.get("k"));
    }

    @Test
    void toPrettyFormat_object() {
        String oneLine = "{\"a\":1,\"b\":2}";
        String pretty = GsonUtils.toPrettyFormat(oneLine);
        assertTrue(pretty.contains("\n"));
        assertTrue(pretty.contains("\"a\""));
    }

    @Test
    void toPrettyFormat_array() {
        String oneLine = "[1,2,3]";
        String pretty = GsonUtils.toPrettyFormat(oneLine);
        assertTrue(pretty.contains("\n"));
    }

    @Test
    void toPrettyFormat_primitive_unchanged() {
        assertEquals("42", GsonUtils.toPrettyFormat("42"));
    }

    @Test
    void isValidJson() {
        assertFalse(GsonUtils.isValidJson(null));
        assertFalse(GsonUtils.isValidJson(""));
        assertFalse(GsonUtils.isValidJson("   "));
        assertFalse(GsonUtils.isValidJson("{"));
        assertTrue(GsonUtils.isValidJson("{}"));
        assertTrue(GsonUtils.isValidJson("[]"));
        assertTrue(GsonUtils.isValidJson("{\"a\":1}"));
    }

    @Test
    void getRawValue_stringAndNumber() {
        assertEquals("hi", GsonUtils.getRawValue("{\"msg\":\"hi\"}", "msg"));
        assertEquals("42", GsonUtils.getRawValue("{\"n\": 42}", "n"));
    }

    @Test
    void getRawValue_nullPrimitive() {
        assertNull(GsonUtils.getRawValue("{\"x\":null}", "x"));
    }

    @Test
    void getRawValue_objectAndArray() {
        assertEquals("{\"b\":1}", GsonUtils.getRawValue("{\"a\":{\"b\":1}}", "a"));
        assertEquals("[1,2]", GsonUtils.getRawValue("{\"a\":[1,2]}", "a"));
    }

    @Test
    void getRawValue_missingTag() {
        assertEquals(StringUtils.EMPTY, GsonUtils.getRawValue("{\"a\":1}", "z"));
    }

    @Test
    void getRawValue_escapedString_preservesBackslashInOutput() {
        // getStringValue copies escape sequences literally (backslash + quote), not unescaped.
        assertEquals("a\\\"b", GsonUtils.getRawValue("{\"k\":\"a\\\"b\"}", "k"));
    }

    @Test
    void mapTypeReference_isMapStringString() {
        assertNotNull(GsonUtils.MAP_TYPE_REFERENCE);
    }

    @Test
    void maxSize_constant() {
        assertEquals(1 << 13, GsonUtils.MAX_SIZE);
    }
}
