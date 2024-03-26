package com.lsnju.base.xml.sax;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.lsnju.base.xml.sax.event.BodyEvent;
import com.lsnju.base.xml.sax.event.EndEvent;
import com.lsnju.base.xml.sax.event.SaxEvent;
import com.lsnju.base.xml.sax.event.StartEvent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2023/3/24 13:56
 * @version V1.0
 */
@Slf4j
public class TpSaxEventRecorder extends DefaultHandler {

    private final ElementPath elementPath;
    @Getter
    private final List<SaxEvent> saxEventList = new ArrayList<>();
    @Getter
    private Locator locator;

    public TpSaxEventRecorder() {
        this(new ElementPath());
    }

    public TpSaxEventRecorder(ElementPath elementPath) {
        this.elementPath = elementPath;
    }

    public final void recordEvents(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
        recordEvents(new InputSource(inputStream));
    }

    public final void recordEvents(InputSource inputSource) throws IOException, SAXException, ParserConfigurationException {
        SAXParser saxParser = buildSaxParser();
        saxParser.parse(inputSource, this);
    }

    private SAXParser buildSaxParser() throws SAXException, ParserConfigurationException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(false);
        spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        spf.setNamespaceAware(true);
        return spf.newSAXParser();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String tagName = getTagName(localName, qName);
        elementPath.push(tagName);
        ElementPath current = elementPath.duplicate();
        saxEventList.add(new StartEvent(current, uri, localName, qName, attributes, getLocator()));
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String bodyStr = new String(ch, start, length);
        SaxEvent lastEvent = getLastEvent();
        if (lastEvent instanceof BodyEvent) {
            BodyEvent be = (BodyEvent) lastEvent;
            be.append(bodyStr);
        } else {
            // ignore space only text if the previous event is not a BodyEvent
            if (!isSpaceOnly(bodyStr)) {
                saxEventList.add(new BodyEvent(bodyStr, getLocator()));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        saxEventList.add(new EndEvent(uri, localName, qName, getLocator()));
        elementPath.pop();
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        final String systemId = locator.getSystemId();
        final String publicId = locator.getPublicId();
        final int lineNumber = locator.getLineNumber();
        final int columnNumber = locator.getColumnNumber();
        log.debug("systemId={}, publicId={} [{},{}]", systemId, publicId, lineNumber, columnNumber);
        this.locator = locator;
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        //
        log.error(String.format("%s", e.getMessage()), e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        //
        log.error(String.format("%s", e.getMessage()), e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        //
        log.error(String.format("%s", e.getMessage()), e);
    }

    private boolean isSpaceOnly(String bodyStr) {
        //
        return StringUtils.isBlank(bodyStr);
    }

    private SaxEvent getLastEvent() {
        if (saxEventList.isEmpty()) {
            return null;
        }
        int size = saxEventList.size();
        return saxEventList.get(size - 1);
    }

    private String getTagName(String localName, String qName) {
        //
        return StringUtils.defaultIfBlank(localName, qName);
    }
}
