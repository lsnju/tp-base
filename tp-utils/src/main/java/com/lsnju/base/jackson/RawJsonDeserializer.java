package com.lsnju.base.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lisong
 * @since 2020/3/18 20:16
 * @version V1.0
 */
@Slf4j
public class RawJsonDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        // return jp.getCodec().readTree(jp).toString();
        final long begin = jp.getCurrentLocation().getCharOffset();
        jp.skipChildren();
        final long end = jp.getCurrentLocation().getCharOffset();
        // final String json = jp.getCurrentLocation().getSourceRef().toString();
        final String json = jp.getCurrentLocation().contentReference().getRawContent().toString();
        log.debug("{}->{}, {}", begin, end, json);
        return json.substring((int) begin - 1, (int) end);
    }
}
