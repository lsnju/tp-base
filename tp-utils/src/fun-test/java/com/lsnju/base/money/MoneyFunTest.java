package com.lsnju.base.money;

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
    }

    @Test
    void test_002() {
        try {
            RedisSerializer<Object> json = RedisSerializer.json();
            byte[] serialize = json.serialize(new Money("11.1"));
            Objects.requireNonNull(serialize);

            log.info("{}", new String(serialize));
            Object m = json.deserialize(serialize);
            log.info("{}", m);
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }
}
