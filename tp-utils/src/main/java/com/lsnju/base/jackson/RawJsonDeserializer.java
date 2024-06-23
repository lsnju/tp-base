package com.lsnju.base.jackson;

import java.io.IOException;

import org.joor.Reflect;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.IOContext;
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
        final long begin = jp.currentLocation().getCharOffset();
        jp.skipChildren();
        final long end = jp.currentLocation().getCharOffset();
        log.debug("start={}, end={}", begin, end);
        final IOContext ioContext = Reflect.on(jp).get("_ioContext");
        if (ioContext == null) {
            return null;
        }
        final Object rawContent = ioContext.contentReference().getRawContent();
        if (rawContent == null) {
            return null;
        }
        final String json = rawContent.toString();
        log.debug("rawJson={}", json);
        return json.substring((int) begin - 1, (int) end);
    }
}
