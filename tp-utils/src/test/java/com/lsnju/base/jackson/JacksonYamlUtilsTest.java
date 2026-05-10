package com.lsnju.base.jackson;

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

    static class BeanWithKnownField {
        public String known;
    }

    @Test
    void toYml_and_fromYml_roundTrip() {
        SampleBean bean = new SampleBean();
        bean.id = "abc";
        bean.count = 7;

        String yml = JacksonYamlUtils.toYml(bean);
        Assertions.assertNotNull(yml);
        Assertions.assertTrue(yml.contains("id: \"abc\"") || yml.contains("id: abc"));
        Assertions.assertTrue(yml.contains("count: 7"));

        SampleBean back = JacksonYamlUtils.fromYml(yml, SampleBean.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals("abc", back.id);
        Assertions.assertEquals(7, back.count);
    }

    @Test
    void toYml_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> JacksonYamlUtils.toYml(null));
    }

    @Test
    void fromYml_blank_returnsNull() {
        Assertions.assertNull(JacksonYamlUtils.fromYml("", SampleBean.class));
        Assertions.assertNull(JacksonYamlUtils.fromYml("   ", SampleBean.class));
        Assertions.assertNull(JacksonYamlUtils.fromYml(null, SampleBean.class));
    }

    @Test
    void fromYml_nullClazz_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> JacksonYamlUtils.fromYml("id: a", null));
    }

    @Test
    void fromYml_unknownProperty_ignored() {
        String yml = "known: v\nextra: 99\n";
        BeanWithKnownField bean = JacksonYamlUtils.fromYml(yml, BeanWithKnownField.class);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals("v", bean.known);
    }

    @Test
    void fromYml_invalidContent_throws() {
        Assertions.assertThrows(RuntimeException.class, () -> JacksonYamlUtils.fromYml("known: [", BeanWithKnownField.class));
    }
}
