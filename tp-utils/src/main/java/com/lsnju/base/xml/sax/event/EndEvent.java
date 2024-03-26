package com.lsnju.base.xml.sax.event;

import org.xml.sax.Locator;

/**
 *
 * @author lisong
 * @since 2023/3/24 15:17
 * @version V1.0
 */
public class EndEvent extends SaxEvent {

    public EndEvent(String namespaceURI, String localName, String qName, Locator locator) {
        super(namespaceURI, localName, qName, locator);
    }

    @Override
    public String toString() {
        return "  EndEvent(" + getQName() + ")  [" + locator.getLineNumber() + "," + locator.getColumnNumber() + "]";
    }

}
