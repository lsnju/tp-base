package com.lsnju.base.jackson.mask;

import java.io.IOException;
import java.io.Serial;

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
public class MaskingSerializerForGid extends StdSerializer<String> {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final TpStringMaskUtils MASK = new TpStringMaskUtils(6, 4, '*', -1);

    public MaskingSerializerForGid() {
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
