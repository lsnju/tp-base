package com.lsnju.base.jackson;

import java.time.LocalDateTime;
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
public class TpLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public TpLocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializationContext serializerProvider) {
        if (value != null) {
            gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            gen.writeNull();
        }
    }

}
