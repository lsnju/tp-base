package com.lsnju.base.jackson;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;

import com.lsnju.base.util.TpDateUtils;

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
public class TpZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    public TpZonedDateTimeDeserializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        final String valueAsString = jsonParser.getValueAsString();
        if (StringUtils.isNotBlank(valueAsString)) {
            return TpDateUtils.parseToZonedDateTime(valueAsString);
        }
        return null;
    }

}
