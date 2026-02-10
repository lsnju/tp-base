package com.lsnju.base.jackson;

import java.time.ZonedDateTime;
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
public class TpZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    public TpZonedDateTimeSerializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializationContext serializerProvider) {
        if (value != null) {
            gen.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        } else {
            gen.writeNull();
        }
    }

}
