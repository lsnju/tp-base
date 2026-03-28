package com.lsnju.base.jackson;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsnju.base.gson.test.TestValue;
import com.lsnju.base.jackson.mask.MaskAnnotationIntrospector;
import com.lsnju.base.money.Money;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:37
 * @version V1.0
 */
@Slf4j
public class MaskFunTest {

    @Test
    void test_001() throws JsonProcessingException {
        TestValue v = new TestValue();
        v.setId(0);
        v.setName("name");
        v.setAmount(new Money());
        v.setMemo("123456199901011234");
        v.setPhone("123456199901011234");
        v.setGid("123456199901011234");
        v.setDesc1("123456199901011234");
        v.setDesc2("123456199901011234");
        v.setDesc3("123456199901011234");
        v.setDesc4("123456199901011234");
        v.setDesc5("123456199901011234");
        v.setDesc6("123456199901011234");

        ObjectMapper mapper = JacksonUtils.PRETTY_MAPPER.copy();
        mapper.setAnnotationIntrospector(new MaskAnnotationIntrospector());
        String json = mapper.writeValueAsString(v);
        log.info("{}", json);
    }

}
