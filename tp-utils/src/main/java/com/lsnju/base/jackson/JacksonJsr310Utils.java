package com.lsnju.base.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.lsnju.base.money.Money;

import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

/**
 *
 * @author lis614
 * @since 2025/1/6 14:19
 * @version V1.0
 */
public class JacksonJsr310Utils {

    public static final SimpleModule JAVA_TIME_MODULE = javaTimeModule();

    public static SimpleModule javaTimeModule() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(Money.class, new MoneySerializer());
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        module.addSerializer(LocalDateTime.class, new TpLocalDateTimeSerializer());
        module.addSerializer(ZonedDateTime.class, new TpZonedDateTimeSerializer());

        module.addDeserializer(Money.class, new MoneyDeserializer());
        module.addDeserializer(LocalDateTime.class, new TpLocalDateTimeDeserializer());
        module.addDeserializer(ZonedDateTime.class, new TpZonedDateTimeDeserializer());

        return module;
    }

}
