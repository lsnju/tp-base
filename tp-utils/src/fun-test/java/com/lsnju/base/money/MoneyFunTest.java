package com.lsnju.base.money;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.RedisSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/5/31 07:31
 * @version V1.0
 */
@Slf4j
public class MoneyFunTest {

    @Test
    void test_001() {
        log.info("{}", new Money());
        log.info("{}", new Money(11.11));
        log.info("{}", new Money("-11.11"));
    }

    @Test
    void test_002() {
        try {
            RedisSerializer<Object> json = RedisSerializer.json();
            byte[] serialize = json.serialize(new Money("11.1"));
            Objects.requireNonNull(serialize);

            log.info("{}", new String(serialize));
            log.info("{}", json.deserialize(serialize));
            log.info("{}", json.deserialize("{\"@class\":\"com.lsnju.base.money.Money\",\"cent\":1110,\"currency\":\"CNY\"}".getBytes(StandardCharsets.UTF_8)));
            log.info("{}", json.deserialize("{\"@class\":\"com.lsnju.base.money.Money\",\"cent\":1110}".getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

}
