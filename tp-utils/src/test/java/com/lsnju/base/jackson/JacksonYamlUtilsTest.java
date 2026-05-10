package com.lsnju.base.jackson;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JacksonYamlUtils}.
 */
class JacksonYamlUtilsTest {

    static class SampleBean {
        public String id;
        public int count;
    }

    @Test
    void toYml_then_fromYml_roundTrip() {
        SampleBean original = new SampleBean();
        original.id = "y1";
        original.count = 3;
        String yml = JacksonYamlUtils.toYml(original);
        Assertions.assertNotNull(yml);
        Assertions.assertTrue(yml.contains("id:"), yml);
        Assertions.assertTrue(yml.contains("y1"), yml);
        Assertions.assertTrue(yml.contains("count:"), yml);
        Assertions.assertTrue(yml.contains("\n"), "INDENT_OUTPUT should produce multi-line YAML");

        SampleBean parsed = JacksonYamlUtils.fromYml(yml, SampleBean.class);
        Assertions.assertNotNull(parsed);
        Assertions.assertEquals("y1", parsed.id);
        Assertions.assertEquals(3, parsed.count);
    }

    @Test
    void toYml_map_roundTrip() {
        Map<String, String> map = Map.of("a", "1", "b", "2");
        String yml = JacksonYamlUtils.toYml(map);
        Assertions.assertNotNull(yml);
        @SuppressWarnings("unchecked")
        Map<String, String> back = JacksonYamlUtils.fromYml(yml, Map.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals("1", back.get("a"));
        Assertions.assertEquals("2", back.get("b"));
    }

    @Test
    void toYml_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> JacksonYamlUtils.toYml(null));
    }

    @Test
    void fromYml_blank_or_null_returnsNull() {
        Assertions.assertNull(JacksonYamlUtils.fromYml(null, SampleBean.class));
        Assertions.assertNull(JacksonYamlUtils.fromYml("", SampleBean.class));
        Assertions.assertNull(JacksonYamlUtils.fromYml("   ", SampleBean.class));
    }

    @Test
    void fromYml_nullClazz_throws() {
        Assertions.assertThrows(NullPointerException.class,
            () -> JacksonYamlUtils.fromYml("id: x", (Class<SampleBean>) null));
    }

    @Test
    void fromYml_unknownPropertiesIgnored() {
        String yml = """
            id: with-extra
            count: 7
            unexpected: ignored
            """;
        SampleBean parsed = JacksonYamlUtils.fromYml(yml, SampleBean.class);
        Assertions.assertNotNull(parsed);
        Assertions.assertEquals("with-extra", parsed.id);
        Assertions.assertEquals(7, parsed.count);
    }
}
