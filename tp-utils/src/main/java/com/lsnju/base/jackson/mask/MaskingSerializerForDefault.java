package com.lsnju.base.jackson.mask;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.lsnju.base.util.TpStringMaskUtils;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:24
 * @version V1.0
 */
public class MaskingSerializerForDefault extends StdSerializer<String> {

    private static final TpStringMaskUtils MASK = new TpStringMaskUtils(4, 4, '*', 2);

    public MaskingSerializerForDefault() {
        super(String.class);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            gen.writeString(MASK.mask(value));
        } else {
            gen.writeNull();
        }
    }

}
