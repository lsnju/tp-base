package com.lsnju.base.jackson;

import java.io.IOException;
import java.time.LocalTime;
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
public class TpLocalTimeSerializer extends StdSerializer<LocalTime> {

    /** */
    private static final long serialVersionUID = 1387644815657332923L;

    public TpLocalTimeSerializer() {
        super(LocalTime.class);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_TIME));
        } else {
            gen.writeNull();
        }
    }

}
