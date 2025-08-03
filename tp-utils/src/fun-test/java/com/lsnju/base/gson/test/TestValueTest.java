package com.lsnju.base.gson.test;

import org.junit.jupiter.api.Test;

import com.lsnju.base.gson.GsonUtils;
import com.lsnju.base.money.Money;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:10
 * @version V1.0
 */
@Slf4j
public class TestValueTest {

    @Test
    void test_001() {
        TestValue v = new TestValue();
        v.setId(0);
        v.setName("name");
        v.setAmount(new Money());
        v.setMemo("12345678");
        log.info("{}", GsonUtils.toJsonPretty(v));
    }

}
