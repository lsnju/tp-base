package com.lsnju.base.jackson;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.reflect.TypeToken;
import com.lsnju.base.money.Money;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.module.SimpleModule;

/**
 * Unit tests for {@link JacksonUtils}.
 */
class JacksonUtilsTest {

    static class SampleBean {
        public String id;
        public int count;
    }

    static class BeanWithUnknown {
        public String known;
    }

    @Test
    void getDefaultModule_and_getSimpleModule_notNull() {
        SimpleModule m1 = JacksonUtils.getDefaultModule();
        SimpleModule m2 = JacksonUtils.getSimpleModule();
        Assertions.assertNotNull(m1);
        Assertions.assertNotNull(m2);
    }

    @Test
    void defaultMapper_and_prettyMapper_notNull() {
        Assertions.assertNotNull(JacksonUtils.DEFAULT_MAPPER);
        Assertions.assertNotNull(JacksonUtils.PRETTY_MAPPER);
    }

    @Test
    void toJson_null_returnsNull() {
        Assertions.assertNull(JacksonUtils.toJson(null));
    }

    @Test
    void toJsonPretty_null_returnsNull() {
        Assertions.assertNull(JacksonUtils.toJsonPretty(null));
    }

    @Test
    void toJson_roundTrip_map() {
        Map<String, String> map = Map.of("a", "1", "b", "2");
        String json = JacksonUtils.toJson(map);
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("\"a\""));
        Map<String, String> back = JacksonUtils.fromJson(json, JacksonUtils.MAP_TYPE_REFERENCE);
        Assertions.assertNotNull(back);
        Assertions.assertEquals("1", back.get("a"));
    }

    @Test
    void toJsonPretty_containsNewlines() {
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("k", Map.of("inner", 1));
        String pretty = JacksonUtils.toJsonPretty(nested);
        Assertions.assertNotNull(pretty);
        Assertions.assertTrue(pretty.contains("\n"));
    }

    @Test
    void toMap_fromObject() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("x", "y");
        Map<String, String> out = JacksonUtils.toMap(map);
        Assertions.assertNotNull(out);
        Assertions.assertEquals("y", out.get("x"));
    }

    @Test
    void toMap_fromJsonString() {
        String json = "{\"p\":\"q\"}";
        Map<String, String> out = JacksonUtils.toMap(json);
        Assertions.assertNotNull(out);
        Assertions.assertEquals("q", out.get("p"));
    }

    @Test
    void toMap_null_returnsNull() {
        Assertions.assertNull(JacksonUtils.toMap(null));
    }

    @Test
    void toMap_blankString_returnsNull() {
        Assertions.assertNull(JacksonUtils.toMap(""));
        Assertions.assertNull(JacksonUtils.toMap("   "));
    }

    @Test
    void fromJson_class_blank_returnsNull() {
        Assertions.assertNull(JacksonUtils.fromJson("", String.class));
        Assertions.assertNull(JacksonUtils.fromJson("  ", String.class));
        Assertions.assertNull(JacksonUtils.fromJson(null, String.class));
    }

    @Test
    void fromJson_class_nullClazz_throws() {
        Assertions.assertThrows(NullPointerException.class,
            () -> JacksonUtils.fromJson("{}", (Class<SampleBean>) null));
    }

    @Test
    void fromJson_class_deserializes() {
        SampleBean bean = JacksonUtils.fromJson("{\"id\":\"z\",\"count\":7}", SampleBean.class);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals("z", bean.id);
        Assertions.assertEquals(7, bean.count);
    }

    @Test
    void fromJson_typeReference_list() {
        List<String> list = JacksonUtils.fromJson("[\"a\",\"b\"]", new TypeReference<List<String>>() {});
        Assertions.assertNotNull(list);
        Assertions.assertEquals(List.of("a", "b"), list);
    }

    @Test
    void fromJson_typeReference_blank_returnsNull() {
        Assertions.assertNull(JacksonUtils.fromJson("", new TypeReference<List<String>>() {}));
    }

    @Test
    void fromJson_javaType_list() {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> list = JacksonUtils.fromJson("[1,2,3]", listType);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(List.of(1, 2, 3), list);
    }

    @Test
    void fromJson_javaType_blank_returnsNull() {
        Type listType = new TypeToken<List<String>>() {}.getType();
        Assertions.assertNull(JacksonUtils.fromJson("", listType));
    }

    @Test
    void fromJson_javaType_nullType_throws() {
        Assertions.assertThrows(NullPointerException.class,
            () -> JacksonUtils.fromJson("[]", (Type) null));
    }

    @Test
    void money_roundTrip_asJsonString() {
        Money m = new Money(2, 50);
        String json = JacksonUtils.toJson(m);
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.startsWith("\""));
        Money back = JacksonUtils.fromJson(json, Money.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals(m.getCent(), back.getCent());
    }

    @Test
    void localDateTime_roundTrip() {
        LocalDateTime ldt = LocalDateTime.of(2024, 6, 1, 14, 30, 0);
        String json = JacksonUtils.toJson(ldt);
        Assertions.assertNotNull(json);
        LocalDateTime back = JacksonUtils.fromJson(json, LocalDateTime.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals(ldt, back);
    }

    @Test
    void localDate_roundTrip() {
        LocalDate d = LocalDate.of(2024, 3, 15);
        String json = JacksonUtils.toJson(d);
        Assertions.assertNotNull(json);
        LocalDate back = JacksonUtils.fromJson(json, LocalDate.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals(d, back);
    }

    @Test
    void localTime_roundTrip() {
        LocalTime t = LocalTime.of(9, 5, 30);
        String json = JacksonUtils.toJson(t);
        Assertions.assertNotNull(json);
        LocalTime back = JacksonUtils.fromJson(json, LocalTime.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals(t, back);
    }

    @Test
    void zonedDateTime_roundTrip() {
        ZonedDateTime z = ZonedDateTime.parse("2024-01-02T12:00:00+08:00");
        String json = JacksonUtils.toJson(z);
        Assertions.assertNotNull(json);
        ZonedDateTime back = JacksonUtils.fromJson(json, ZonedDateTime.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals(z.toInstant(), back.toInstant());
    }

    @Test
    void fromJson_unknownProperty_ignored() {
        BeanWithUnknown bean = JacksonUtils.fromJson("{\"known\":\"v\",\"extra\":99}", BeanWithUnknown.class);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals("v", bean.known);
    }

}
