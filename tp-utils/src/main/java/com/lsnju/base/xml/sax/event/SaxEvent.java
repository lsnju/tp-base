package com.lsnju.base.xml.sax.event;

import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import lombok.Getter;

/**
 *
 * @author lisong
 * @since 2023/3/24 14:46
 * @version V1.0
 */
@Getter
public class SaxEvent {

    final public String namespaceURI;
    final public String localName;
    final public String qName;
    final public Locator locator;

    public SaxEvent(String namespaceURI, String localName, String qName, Locator locator) {
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.qName = qName;
        // locator impl is used to take a snapshot!
        this.locator = new LocatorImpl(locator);
    }
}
