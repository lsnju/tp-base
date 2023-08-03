package com.lsnju.base.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.lsnju.base.money.Money;

/**
 *
 * @author ls
 * @since 2021/3/25 21:13
 * @version V1.0
 */
public class MoneyAdapter extends TypeAdapter<Money> {
    @Override
    public void write(JsonWriter writer, Money value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value.toString());
    }

    @Override
    public Money read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return new Money(reader.nextString());
    }
}
