package com.lsnju.base.xml.sax.event;

import org.xml.sax.Locator;

/**
 *
 * @author lisong
 * @since 2023/3/24 15:16
 * @version V1.0
 */
public class BodyEvent extends SaxEvent {

    private String text;

    public BodyEvent(String text, Locator locator) {
        super(null, null, null, locator);
        this.text = text;
    }

    /**
     * Always trim trailing spaces from the body text.
     */
    public String getText() {
        if (text != null) {
            return text.trim();
        }
        return null;
    }

    @Override
    public String toString() {
        return "BodyEvent(" + getText() + ")" + locator.getLineNumber() + "," + locator.getColumnNumber();
    }

    public void append(String str) {
        text += str;
    }

}
