package com.lsnju.base.xml.sax.event;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.AttributesImpl;

import com.lsnju.base.xml.sax.ElementPath;

import lombok.Getter;

/**
 *
 * @author lisong
 * @since 2023/3/24 15:11
 * @version V1.0
 */
public class StartEvent extends SaxEvent {

    @Getter
    final public Attributes attributes;
    final public ElementPath elementPath;

    public StartEvent(ElementPath elementPath, String namespaceURI, String localName, String qName, Attributes attributes,
                      Locator locator) {
        super(namespaceURI, localName, qName, locator);
        this.attributes = new AttributesImpl(attributes);
        this.elementPath = elementPath;
    }

    public String getTagPath() {
        return elementPath.getTagPath();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("StartEvent(");
        b.append(getQName());
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                b.append(' ');
                b.append(attributes.getLocalName(i)).append("=\"").append(attributes.getValue(i)).append("\"");
            }
        }
        b.append(")  [");
        b.append(locator.getLineNumber());
        b.append(",");
        b.append(locator.getColumnNumber());
        b.append("]");
        return b.toString();
    }

}
