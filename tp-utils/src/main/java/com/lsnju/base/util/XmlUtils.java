package com.lsnju.base.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-20 12:51:22
 * @version V1.0
 */
@Slf4j
public class XmlUtils {

    public static final String CDATA_START = "<![CDATA[";
    public static final String CDATA_END = "]]>";

    public static <T> T fromXml(String xml, Class<T> clazz) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(), new InputSource(new StringReader(xml)));
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return clazz.cast(unmarshaller.unmarshal(xmlSource));
        } catch (Exception e) {
            log.warn(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    public static String toXml(Object obj) {
        try {
            JAXBContext jc = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            marshaller.marshal(obj, sw);
            return sw.toString();
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
            return null;
        }
    }

    // ................

    public static String getTagContent(String xml, String tag) {
        final String startTag = String.format("<%s>", tag);
        final String endTag = String.format("</%s>", tag);
        final int startIdx = StringUtils.indexOf(xml, startTag);
        final int endIdx = StringUtils.indexOf(xml, endTag);
        if (startIdx >= 0 && endIdx > 0) {
            final String content = StringUtils.substring(xml, startIdx + startTag.length(), endIdx);
            if (StringUtils.startsWith(content, CDATA_START)) {
                return StringUtils.substring(content, CDATA_START.length(), -CDATA_END.length());
            }
            return content;
        }
        return StringUtils.EMPTY;
    }

    public static String simpleFormat(String xml) {
        return RegExUtils.replacePattern(xml, ">\\s*<", ">\n<");
    }

    public static String trimFormat(String xml) {
        return format(RegExUtils.replacePattern(xml, ">\\s*<", "><"));
    }

    public static String format(String xml) {
        return prettyFormat(xml, "2");
    }

    private static String prettyFormat(String input, String indent) {
        Source xmlInput = new StreamSource(new StringReader(input));
        try (StringWriter stringWriter = new StringWriter()) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent);
            transformer.transform(xmlInput, new StreamResult(stringWriter));
            return stringWriter.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
