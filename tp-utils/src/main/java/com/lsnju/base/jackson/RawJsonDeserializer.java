package com.lsnju.base.jackson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lisong
 * @since 2020/3/18 20:16
 * @version V1.0
 */
@Slf4j
public class RawJsonDeserializer implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // TODO
        return "";
    }

    //    @Override
//    public String deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
//        // return jp.getCodec().readTree(jp).toString();
//        final long begin = jp.currentLocation().getCharOffset();
//        jp.skipChildren();
//        final long end = jp.currentLocation().getCharOffset();
//        log.debug("start={}, end={}", begin, end);
//        final IOContext ioContext = Reflect.on(jp).get("_ioContext");
//        if (ioContext == null) {
//            return null;
//        }
//        final Object rawContent = ioContext.contentReference().getRawContent();
//        if (rawContent == null) {
//            return null;
//        }
//        final String json = rawContent.toString();
//        log.debug("rawJson={}", json);
//        return json.substring((int) begin - 1, (int) end);
//    }
}
