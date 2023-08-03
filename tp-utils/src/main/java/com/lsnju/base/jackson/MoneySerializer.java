package com.lsnju.base.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.lsnju.base.money.Money;

/**
 * @author lisong
 * @since 2020/3/19 14:57
 * @version V1.0
 */
public class MoneySerializer extends StdSerializer<Money> {

    /** */
    private static final long serialVersionUID = 4216204308988360618L;

    public MoneySerializer() {
        super(Money.class);
    }

    @Override
    public void serialize(Money value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
        if (value != null) {
            gen.writeString(value.toString());
        } else {
            gen.writeNull();
        }
    }
}
