package com.lsnju.base.jackson;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 *
 * @author ls
 * @since 2024/11/2 11:03
 * @version V1.0
 */
public class TpLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    /** */
    private static final long serialVersionUID = 1387644815657332923L;

    public TpLocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final String valueAsString = jsonParser.getValueAsString();
        if (StringUtils.isNotBlank(valueAsString)) {
            return LocalDateTime.parse(valueAsString);
        }
        return null;
    }

}
