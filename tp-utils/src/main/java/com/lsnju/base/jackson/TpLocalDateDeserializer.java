package com.lsnju.base.jackson;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 *
 * @author ls
 * @since 2024/11/2 10:28
 * @version V1.0
 */
public class TpLocalDateDeserializer extends StdDeserializer<LocalDate> {

    public TpLocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        final String valueAsString = jsonParser.getValueAsString();
        if (StringUtils.isNotBlank(valueAsString)) {
            return LocalDate.parse(valueAsString);
        }
        return null;
    }

}
