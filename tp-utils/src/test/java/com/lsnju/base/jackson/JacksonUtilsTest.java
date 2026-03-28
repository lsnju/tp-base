package com.lsnju.base.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lsnju.base.money.Money;

/**
 * Unit tests for {@link JacksonUtils}.
 *
 * @author ls
 */
class JacksonUtilsTest {

    enum Color {
        RED,
        BLUE
    }

    static class Holder {
        public Color color;
    }

    static class Person {
        public String name;
        public int age;
        public LocalDateTime at;

        public Person() {}

        public Person(String name, int age, LocalDateTime at) {
            this.name = name;
            this.age = age;
            this.at = at;
        }
    }

    @Test
    void mappers_and_module_notNull() {
        assertNotNull(JacksonUtils.DEFAULT_MAPPER);
        assertNotNull(JacksonUtils.PRETTY_MAPPER);
        assertNotNull(JacksonUtils.MAP_TYPE_REFERENCE);
        assertNotNull(JacksonUtils.getDefaultModule());
    }

    @Test
    void toJson_null_returnsNull() {
        assertNull(JacksonUtils.toJson(null));
    }

    @Test
    void toJsonPretty_null_returnsNull() {
        assertNull(JacksonUtils.toJsonPretty(null));
    }

    @Test
    void toJsonPretty_hasIndent_and_nonNullOmitted() throws IOException {
        Map<String, Object> m = new HashMap<>();
        m.put("a", 1);
        m.put("b", null);
        String pretty = JacksonUtils.toJsonPretty(m);
        assertTrue(pretty.contains("\n"));
        assertFalse(pretty.contains("\"b\""));
        Map<String, String> flat = JacksonUtils.toMap(m);
        assertEquals("1", flat.get("a"));
    }

    @Test
    void fromJson_blank_returnsNull() {
        assertNull(JacksonUtils.fromJson("", Person.class));
        assertNull(JacksonUtils.fromJson("   ", Person.class));
        assertNull(JacksonUtils.fromJson(null, Person.class));
    }

    @Test
    void fromJson_invalid_returnsNull() {
        assertNull(JacksonUtils.fromJson("{", Person.class));
    }

    @Test
    void fromJson_class_null_throws() {
        assertThrows(NullPointerException.class, () -> JacksonUtils.fromJson("{}", (Class<Person>) null));
    }

    @Test
    void fromJson_typeReference_null_throws() {
        assertThrows(NullPointerException.class, () -> JacksonUtils.fromJson("{}", (TypeReference<Map<String, String>>) null));
    }

    @Test
    void fromJson_type_null_throws() {
        assertThrows(NullPointerException.class, () -> JacksonUtils.fromJson("{}", (Type) null));
    }

    @Test
    void fromJson_typeReference_list() {
        List<String> list = JacksonUtils.fromJson("[\"a\",\"b\"]", new TypeReference<List<String>>() {});
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    void fromJson_reflectType_list() {
        Type listType = new TypeReference<List<String>>() {}.getType();
        List<String> list = JacksonUtils.fromJson("[\"x\"]", listType);
        assertEquals(1, list.size());
        assertEquals("x", list.get(0));
    }

    @Test
    void unknownEnum_deserializesAsNull() {
        Holder h = JacksonUtils.fromJson("{\"color\":\"MAGENTA\"}", Holder.class);
        assertNotNull(h);
        assertNull(h.color);
    }

    @Test
    void money_roundTrip() {
        Money m = new Money("12.34");
        String json = JacksonUtils.toJson(m);
        assertNotNull(json);
        Money back = JacksonUtils.fromJson(json, Money.class);
        assertEquals(m, back);
    }

    @Test
    void person_roundTrip_whenJsr310Present() {
        if (!JacksonUtils.WITH_JSR310) {
            return;
        }
        LocalDateTime at = LocalDateTime.of(2024, 6, 1, 12, 30, 45);
        Person original = new Person("bob", 30, at);
        String json = JacksonUtils.toJson(original);
        Person back = JacksonUtils.fromJson(json, Person.class);
        assertEquals(original.name, back.name);
        assertEquals(original.age, back.age);
        assertEquals(original.at, back.at);
    }

    @Test
    void toMap_fromJsonString() throws IOException {
        Map<String, String> map = JacksonUtils.toMap("{\"a\":\"b\",\"c\":\"d\"}");
        assertEquals("b", map.get("a"));
        assertEquals("d", map.get("c"));
    }

    @Test
    void toMap_fromObject() throws IOException {
        Map<String, String> in = new HashMap<>();
        in.put("x", "1");
        in.put("y", "2");
        Map<String, String> map = JacksonUtils.toMap(in);
        assertEquals("1", map.get("x"));
        assertEquals("2", map.get("y"));
    }

    @Test
    void getRawValue_stringField() throws IOException {
        assertEquals("hi", JacksonUtils.getRawValue("{\"msg\":\"hi\"}", new String[] {"msg"}));
    }

    @Test
    void getRawValue_nestedPath() throws IOException {
        assertEquals("v", JacksonUtils.getRawValue("{\"a\":{\"b\":\"v\"}}", new String[] {"a", "b"}));
    }

    @Test
    void getRawValue_numberAndBoolean() throws IOException {
        assertEquals("42", JacksonUtils.getRawValue("{\"n\":42}", new String[] {"n"}));
        assertEquals("true", JacksonUtils.getRawValue("{\"f\":true}", new String[] {"f"}));
    }

    @Test
    void getRawValue_nullPrimitive() throws IOException {
        assertNull(JacksonUtils.getRawValue("{\"x\":null}", new String[] {"x"}));
    }

    @Test
    void getRawValue_objectSlice() throws IOException {
        String json = "{\"a\":{\"b\":1}}";
        String raw = JacksonUtils.getRawValue(json, new String[] {"a"});
        assertTrue(raw.contains("\"b\""));
        assertTrue(raw.contains("1"));
    }

    @Test
    void getRawValue_arraySlice() throws IOException {
        String json = "{\"a\":[1,2]}";
        String raw = JacksonUtils.getRawValue(json, new String[] {"a"});
        assertTrue(raw.startsWith("["));
        assertTrue(raw.endsWith("]"));
    }

    @Test
    void getRawValue_missingKey() throws IOException {
        assertEquals(StringUtils.EMPTY, JacksonUtils.getRawValue("{\"a\":1}", new String[] {"z"}));
    }

    @Test
    void getRawValue_rootNotObject() throws IOException {
        assertEquals(StringUtils.EMPTY, JacksonUtils.getRawValue("[1,2]", new String[] {"a"}));
    }

    @Test
    void getRawValue_emptyPath() throws IOException {
        assertEquals(StringUtils.EMPTY, JacksonUtils.getRawValue("{}", new String[0]));
    }
}
