package com.lsnju.base.jackson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Unit tests for {@link JacksonXmlUtils}.
 */
class JacksonXmlUtilsTest {

    @JacksonXmlRootElement(localName = "sample")
    static class SampleBean {
        @JacksonXmlProperty(localName = "id")
        public String id;
        @JacksonXmlProperty(localName = "count")
        public int count;
    }

    @JacksonXmlRootElement(localName = "catalog")
    static class Catalog {
        @JacksonXmlElementWrapper(localName = "items")
        @JacksonXmlProperty(localName = "item")
        public List<String> items;
    }

    @Test
    void toXml_containsIndentation() throws JsonProcessingException {
        SampleBean bean = new SampleBean();
        bean.id = "a1";
        bean.count = 2;
        String xml = JacksonXmlUtils.toXml(bean);
        Assertions.assertNotNull(xml);
        Assertions.assertTrue(xml.contains("<id>"), xml);
        Assertions.assertTrue(xml.contains("a1"), xml);
        Assertions.assertTrue(xml.contains("<count>"), xml);
        Assertions.assertTrue(xml.contains("2"), xml);
        Assertions.assertTrue(xml.contains("\n"), "INDENT_OUTPUT should add newlines");
    }

    @Test
    void fromXml_class_roundTrip() throws IOException {
        SampleBean original = new SampleBean();
        original.id = "x";
        original.count = 99;
        String xml = JacksonXmlUtils.toXml(original);
        SampleBean parsed = JacksonXmlUtils.fromXml(xml, SampleBean.class);
        Assertions.assertNotNull(parsed);
        Assertions.assertEquals("x", parsed.id);
        Assertions.assertEquals(99, parsed.count);
    }

    @Test
    void fromXml_typeReference_listInsideRoot() throws IOException {
        Catalog catalog = new Catalog();
        catalog.items = List.of("one", "two");
        String xml = JacksonXmlUtils.toXml(catalog);
        Catalog back = JacksonXmlUtils.fromXml(xml, new TypeReference<Catalog>() {});
        Assertions.assertNotNull(back);
        Assertions.assertNotNull(back.items);
        Assertions.assertEquals(2, back.items.size());
        Assertions.assertEquals("one", back.items.get(0));
        Assertions.assertEquals("two", back.items.get(1));
    }

    @Test
    void fromXml_unknownPropertiesIgnored() throws IOException {
        String xml = """
            <sample>
              <id>with-extra</id>
              <count>1</count>
              <unexpected>ignored</unexpected>
            </sample>
            """;
        SampleBean parsed = JacksonXmlUtils.fromXml(xml, SampleBean.class);
        Assertions.assertEquals("with-extra", parsed.id);
        Assertions.assertEquals(1, parsed.count);
    }

    @Test
    void fromXml_typeReference_map() throws IOException {
        String xml = """
            <LinkedHashMap>
              <k1>v1</k1>
              <k2>v2</k2>
            </LinkedHashMap>
            """;
        Map<String, String> map = JacksonXmlUtils.fromXml(xml, new TypeReference<Map<String, String>>() {});
        Assertions.assertEquals("v1", map.get("k1"));
        Assertions.assertEquals("v2", map.get("k2"));
    }
}
