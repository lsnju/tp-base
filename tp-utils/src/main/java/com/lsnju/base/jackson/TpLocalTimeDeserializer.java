package com.lsnju.base.jackson;

import java.io.IOException;
import java.time.LocalTime;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 *
 * @author ls
 * @since 2024/11/2 10:28
 * @version V1.0
 */
public class TpLocalTimeDeserializer extends StdDeserializer<LocalTime> {

    /** */
    private static final long serialVersionUID = 1387644815657332923L;

    public TpLocalTimeDeserializer() {
        super(LocalTime.class);
    }

    @Override
    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final String valueAsString = jsonParser.getValueAsString();
        if (StringUtils.isNotBlank(valueAsString)) {
            return LocalTime.parse(valueAsString);
        }
        return null;
    }

}
