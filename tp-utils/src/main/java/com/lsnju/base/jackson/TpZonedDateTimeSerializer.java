package com.lsnju.base.jackson;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 *
 * @author ls
 * @since 2024/11/2 10:28
 * @version V1.0
 */
public class TpZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    /** */
    private static final long serialVersionUID = 1387644815657332923L;

    public TpZonedDateTimeSerializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            gen.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        } else {
            gen.writeNull();
        }
    }
}
