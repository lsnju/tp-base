package com.lsnju.base.gson;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:03
 * @version V1.0
 */
public abstract class MaskedTypeAdapterFactory implements TypeAdapterFactory {

    private final int exposeFront;
    private final int exposeRear;
    private final char mask;

    protected MaskedTypeAdapterFactory(final int exposeFront, final int exposeRear, final char mask) {
        this.exposeFront = exposeFront;
        this.exposeRear = exposeRear;
        this.mask = mask;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() != String.class) {
            return null;
        }
        final TypeAdapter<String> delegate = (TypeAdapter<String>) gson.getAdapter(typeToken);
        final TypeAdapter<String> typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(final JsonWriter out, final String value) throws IOException {
                // mask the value
                final int length = value.length();
                final char[] buffer = value.toCharArray();
                for (int i = exposeFront; i < length - exposeRear; i++) {
                    buffer[i] = mask;
                }
                out.value(new String(buffer));
            }

            @Override
            public String read(final JsonReader in)
                throws IOException {
                return delegate.read(in);
            }
        }.nullSafe();
        return (TypeAdapter<T>) typeAdapter;
    }

}
