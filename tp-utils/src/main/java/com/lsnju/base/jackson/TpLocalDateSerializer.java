package com.lsnju.base.jackson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 *
 * @author ls
 * @since 2024/11/2 10:28
 * @version V1.0
 */
public class TpLocalDateSerializer extends StdSerializer<LocalDate> {

    public TpLocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializationContext serializerProvider) {
        if (value != null) {
            gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            gen.writeNull();
        }
    }

}
