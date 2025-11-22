package com.lsnju.base.jackson;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 *
 * @author ls
 * @since 2024/11/2 11:03
 * @version V1.0
 */
public class TpLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public TpLocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        final String valueAsString = jsonParser.getValueAsString();
        if (StringUtils.isNotBlank(valueAsString)) {
            return LocalDateTime.parse(valueAsString);
        }
        return null;
    }

}
