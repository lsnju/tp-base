package com.lsnju.base.jackson;

import com.lsnju.base.money.Money;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 * @author lisong
 * @since 2020/3/19 14:57
 * @version V1.0
 */
public class MoneySerializer extends StdSerializer<Money> {

    public MoneySerializer() {
        super(Money.class);
    }

    @Override
    public void serialize(Money value, JsonGenerator gen, SerializationContext provider) throws JacksonException {
        if (value != null) {
            gen.writeString(value.toString());
        } else {
            gen.writeNull();
        }
    }

}
