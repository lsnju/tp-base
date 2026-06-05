package com.lsnju.base.jackson;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import tools.jackson.core.type.TypeReference;

/**
 * Unit tests for {@link JacksonXmlUtils}.
 */
class JacksonXmlUtilsTest {

    static class SampleBean {
        public String id;
        public int count;
    }

    static class BeanWithKnownField {
        public String known;
    }

    @Test
    void toXml_and_fromXml_class_roundTrip() {
        SampleBean bean = new SampleBean();
        bean.id = "abc";
        bean.count = 7;

        String xml = JacksonXmlUtils.toXml(bean);
        Assertions.assertNotNull(xml);
        Assertions.assertTrue(xml.contains("<id>abc</id>"));
        Assertions.assertTrue(xml.contains("<count>7</count>"));

        SampleBean back = JacksonXmlUtils.fromXml(xml, SampleBean.class);
        Assertions.assertNotNull(back);
        Assertions.assertEquals("abc", back.id);
        Assertions.assertEquals(7, back.count);
    }

    @Test
    void toXml_prettyOutput_containsNewline() {
        SampleBean bean = new SampleBean();
        bean.id = "n1";
        bean.count = 1;

        String xml = JacksonXmlUtils.toXml(bean);
        Assertions.assertNotNull(xml);
        Assertions.assertTrue(xml.contains("\n"));
    }

    @Test
    void fromXml_typeReference_list() {
        String xml = "<ArrayList><item><id>a</id><count>1</count></item><item><id>b</id><count>2</count></item></ArrayList>";

        List<SampleBean> list = JacksonXmlUtils.fromXml(xml, new TypeReference<List<SampleBean>>() {});
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("a", list.get(0).id);
        Assertions.assertEquals(2, list.get(1).count);
    }

    @Test
    void fromXml_unknownProperty_ignored() {
        String xml = "<BeanWithKnownField><known>v</known><extra>99</extra></BeanWithKnownField>";
        BeanWithKnownField bean = JacksonXmlUtils.fromXml(xml, BeanWithKnownField.class);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals("v", bean.known);
    }

    @Test
    void fromXml_invalidXml_throws() {
        Assertions.assertThrows(RuntimeException.class,
            () -> JacksonXmlUtils.fromXml("<bad>", SampleBean.class));
    }
}
