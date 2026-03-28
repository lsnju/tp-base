package com.lsnju.base.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import com.google.gson.reflect.TypeToken;

/**
 * Unit tests for {@link TpJsonUtils}.
 *
 * @author ls
 */
class TpJsonUtilsTest {

    static class Box {
        String name;
        int n;

        Box() {}

        Box(String name, int n) {
            this.name = name;
            this.n = n;
        }
    }

    @Test
    void gson_jackson_default_andFactories_notNull() {
        assertNotNull(TpJsonUtils.GSON);
        assertNotNull(TpJsonUtils.JACKSON);
        assertNotNull(TpJsonUtils.DEFAULT);
        assertSame(TpJsonUtils.gson(), TpJsonUtils.GSON);
        assertSame(TpJsonUtils.jackson(), TpJsonUtils.JACKSON);
        assertSame(TpJsonUtils.GSON, TpJsonUtils.DEFAULT);
    }

    @Test
    void toJson_fromJson_roundTrip() throws IOException {
        Box b = new Box("a", 1);
        String json = TpJsonUtils.toJson(b);
        Box back = TpJsonUtils.fromJson(json, Box.class);
        assertEquals(b.name, back.name);
        assertEquals(b.n, back.n);
    }

    @Test
    void toJsonPretty_containsNewlines() throws IOException {
        Map<String, String> m = new HashMap<>();
        m.put("k", "v");
        assertTrue(TpJsonUtils.toJsonPretty(m).contains("\n"));
    }

    @Test
    void toJson_null_throws() {
        assertThrows(NullPointerException.class, () -> TpJsonUtils.toJson(null));
    }

    @Test
    void fromJson_withType_list() throws IOException {
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> list = TpJsonUtils.fromJson("[\"x\",\"y\"]", listType);
        assertEquals(2, list.size());
        assertEquals("x", list.get(0));
    }

    @Test
    void toMap_fromString_andObject() throws IOException {
        Map<String, String> a = TpJsonUtils.toMap("{\"a\":\"1\"}");
        assertEquals("1", a.get("a"));
        Map<String, String> in = new HashMap<>();
        in.put("b", "2");
        Map<String, String> b = TpJsonUtils.toMap(in);
        assertEquals("2", b.get("b"));
    }

    @Test
    void fromMap_toClass() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "n");
        map.put("n", 3.0);
        Box box = TpJsonUtils.fromMap(map, Box.class);
        assertEquals("n", box.name);
        assertEquals(3, box.n);
    }

    @Test
    void toPrettyFormat_object() throws IOException {
        String pretty = TpJsonUtils.toPrettyFormat("{\"z\":9}");
        assertTrue(pretty.contains("\n"));
        assertTrue(pretty.contains("\"z\""));
    }

    @Test
    void isValidJson() {
        assertFalse(TpJsonUtils.isValidJson(null));
        assertFalse(TpJsonUtils.isValidJson(""));
        assertFalse(TpJsonUtils.isValidJson("{"));
        assertTrue(TpJsonUtils.isValidJson("{}"));
    }

    @Test
    void getRawValue() {
        assertEquals("hi", TpJsonUtils.getRawValue("{\"msg\":\"hi\"}", "msg"));
        assertEquals(StringUtils.EMPTY, TpJsonUtils.getRawValue("{\"a\":1}", "z"));
    }

    @Test
    void jackson_fromJson_blank_returnsNull() throws IOException {
        assertNull(TpJsonUtils.jackson().fromJson("", Box.class));
        assertNull(TpJsonUtils.jackson().fromJson("   ", Box.class));
    }

    @Test
    void jackson_toMap_sameAsConvert() throws IOException {
        Map<String, String> in = new HashMap<>();
        in.put("p", "q");
        Map<String, String> out = TpJsonUtils.jackson().toMap(in);
        assertEquals("q", out.get("p"));
    }
}
