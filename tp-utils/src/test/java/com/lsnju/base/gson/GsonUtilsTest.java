package com.lsnju.base.gson;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.reflect.TypeToken;
import com.lsnju.base.money.Money;

/**
 * Unit tests for {@link GsonUtils}.
 */
class GsonUtilsTest {

    /** Plain bean for Gson round-trip tests (no custom TypeAdapter). */
    static class SampleBean {
        String id;
        int count;
    }

    @Test
    void build_returnsNonNullGson() {
        Assertions.assertNotNull(GsonUtils.build(false));
        Assertions.assertNotNull(GsonUtils.build(true));
    }

    @Test
    void toJsonPretty_indentsNestedStructure() {
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("k", Map.of("inner", 1));
        String pretty = GsonUtils.toJsonPretty(nested);
        Assertions.assertTrue(pretty.contains("\n"), "pretty JSON should contain line breaks");
        Assertions.assertTrue(pretty.contains("inner"));
    }

    @Test
    void toJson_roundTrip_simpleMap() {
        Map<String, String> map = Map.of("a", "1", "b", "2");
        String json = GsonUtils.toJson(map);
        Assertions.assertTrue(json.contains("\"a\""));
        Map<String, String> back = GsonUtils.fromJson(json, GsonUtils.MAP_TYPE_REFERENCE);
        Assertions.assertEquals("1", back.get("a"));
        Assertions.assertEquals("2", back.get("b"));
    }

    @Test
    void toMap_fromObject_convertsToStringMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("x", "y");
        Map<String, String> out = GsonUtils.toMap(map);
        Assertions.assertEquals("y", out.get("x"));
    }

    @Test
    void toMap_fromJsonString_parsesObject() {
        String json = "{\"p\":\"q\",\"r\":\"s\"}";
        Map<String, String> out = GsonUtils.toMap(json);
        Assertions.assertEquals("q", out.get("p"));
        Assertions.assertEquals("s", out.get("r"));
    }

    @Test
    void toMap_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> GsonUtils.toMap(null));
    }

    @Test
    void toJson_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> GsonUtils.toJson(null));
    }

    @Test
    void toJsonPretty_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> GsonUtils.toJsonPretty(null));
    }

    @Test
    void fromJson_class_deserializesPlainBean() {
        SampleBean bean = GsonUtils.fromJson("{\"id\":\"a\",\"count\":2}", SampleBean.class);
        Assertions.assertEquals("a", bean.id);
        Assertions.assertEquals(2, bean.count);
    }

    @Test
    void fromJson_class_money_roundTrip_stringForm() {
        Money original = new Money(1, 0);
        String json = GsonUtils.toJson(original);
        Assertions.assertTrue(json.startsWith("\""), "Money is serialized as JSON string");
        Money parsed = GsonUtils.fromJson(json, Money.class);
        Assertions.assertEquals(original.getCent(), parsed.getCent());
    }

    @Test
    void fromJson_type_deserializesList() {
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> list = GsonUtils.fromJson("[\"a\",\"b\"]", listType);
        Assertions.assertEquals(List.of("a", "b"), list);
    }

    @Test
    void fromJson_nullString_returnsNull() {
        Assertions.assertNull(GsonUtils.fromJson(null, String.class));
    }

    @Test
    void fromMap_toClass() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", "map-id");
        map.put("count", 5);
        SampleBean bean = GsonUtils.fromMap(map, SampleBean.class);
        Assertions.assertEquals("map-id", bean.id);
        Assertions.assertEquals(5, bean.count);
    }

    @Test
    void fromMap_toType_stringMap() {
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("k", "v");
        Map<String, String> out = GsonUtils.fromMap(map, type);
        Assertions.assertEquals("v", out.get("k"));
    }

    @Test
    void toPrettyFormat_formatsObject() {
        String compact = "{\"a\":1,\"b\":2}";
        String pretty = GsonUtils.toPrettyFormat(compact);
        Assertions.assertTrue(pretty.contains("\n"));
        Assertions.assertTrue(pretty.contains("\"a\""));
    }

    @Test
    void toPrettyFormat_formatsArray() {
        String compact = "[1,2,3]";
        String pretty = GsonUtils.toPrettyFormat(compact);
        Assertions.assertTrue(pretty.contains("\n"));
        Assertions.assertTrue(pretty.contains("1"));
    }

    @Test
    void toPrettyFormat_primitiveString_unchanged() {
        String s = "\"hello\"";
        Assertions.assertEquals(s, GsonUtils.toPrettyFormat(s));
    }

    @Test
    void isValidJson_blankOrInvalid() {
        Assertions.assertFalse(GsonUtils.isValidJson(null));
        Assertions.assertFalse(GsonUtils.isValidJson(""));
        Assertions.assertFalse(GsonUtils.isValidJson("   "));
        Assertions.assertFalse(GsonUtils.isValidJson("{"));
        Assertions.assertFalse(GsonUtils.isValidJson("not json"));
    }

    @Test
    void isValidJson_objectAndArray() {
        Assertions.assertTrue(GsonUtils.isValidJson("{}"));
        Assertions.assertTrue(GsonUtils.isValidJson("[]"));
        Assertions.assertTrue(GsonUtils.isValidJson("{\"a\":1}"));
    }

    @Test
    void getRawValue_stringField() {
        String json = "{\"name\":\"alice\",\"age\":30}";
        Assertions.assertEquals("alice", GsonUtils.getRawValue(json, "name"));
    }

    @Test
    void getRawValue_numberField() {
        String json = "{\"age\":30}";
        Assertions.assertEquals("30", GsonUtils.getRawValue(json, "age"));
    }

    @Test
    void getRawValue_nullLiteral() {
        String json = "{\"x\": null}";
        Assertions.assertNull(GsonUtils.getRawValue(json, "x"));
    }

    @Test
    void getRawValue_nestedObject() {
        String json = "{\"outer\": {\"inner\": 1}}";
        Assertions.assertEquals("{\"inner\": 1}", GsonUtils.getRawValue(json, "outer"));
    }

    @Test
    void getRawValue_arrayValue() {
        String json = "{\"items\": [1, 2]}";
        Assertions.assertEquals("[1, 2]", GsonUtils.getRawValue(json, "items"));
    }

    @Test
    void getRawValue_missingTag_returnsEmpty() {
        String json = "{\"a\":1}";
        Assertions.assertEquals("", GsonUtils.getRawValue(json, "missing"));
    }

    @Test
    void getRawValue_escapedQuoteInString() {
        String json = "{\"msg\": \"say \\\"hi\\\"\"}";
        String raw = GsonUtils.getRawValue(json, "msg");
        Assertions.assertEquals("say \\\"hi\\\"", raw);
    }

}
