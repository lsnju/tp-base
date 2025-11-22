package com.lsnju.base.jackson.mask;

import com.lsnju.base.util.TpStringMaskUtils;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

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
    public void serialize(String value, JsonGenerator gen, SerializationContext provider) {
        if (value != null) {
            gen.writeString(MASK.mask(value));
        } else {
            gen.writeNull();
        }
    }

}
