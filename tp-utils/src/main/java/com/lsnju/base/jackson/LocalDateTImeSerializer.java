package com.lsnju.base.jackson;

import java.io.IOException;
import java.time.LocalDateTime;
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
public class LocalDateTImeSerializer extends StdSerializer<LocalDateTime> {

    /** */
    private static final long serialVersionUID = 1387644815657332923L;

    protected LocalDateTImeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            gen.writeNull();
        }
    }
}
