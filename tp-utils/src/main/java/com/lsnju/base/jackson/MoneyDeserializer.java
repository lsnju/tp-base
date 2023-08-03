package com.lsnju.base.jackson;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.lsnju.base.money.Money;

/**
 * @author lisong
 * @since 2020/3/19 15:01
 * @version V1.0
 */
public class MoneyDeserializer extends StdDeserializer<Money> {

    /** */
    private static final long serialVersionUID = 1387644815657332923L;

    public MoneyDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String valueAsString = p.getValueAsString();
        if (StringUtils.isNotBlank(valueAsString)) {
            return new Money(valueAsString);
        }
        return null;
    }
}
