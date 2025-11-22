package com.lsnju.base.jackson;

import org.apache.commons.lang3.StringUtils;

import com.lsnju.base.money.Money;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * @author lisong
 * @since 2020/3/19 15:01
 * @version V1.0
 */
public class MoneyDeserializer extends StdDeserializer<Money> {

    public MoneyDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) {
        final String valueAsString = p.getValueAsString();
        if (StringUtils.isNotBlank(valueAsString)) {
            return new Money(valueAsString);
        }
        return null;
    }
}
