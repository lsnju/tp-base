package com.lsnju.base.model;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.lsnju.base.model.vo.MaskVo;
import com.lsnju.base.money.Money;
import com.lsnju.base.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2025/11/23 13:39
 * @version V1.0
 */
@Slf4j
public class MaskJacksonUtilsFunTest {

    @Test
    void test_001() {
        MaskVo v = new MaskVo();
        v.setId(22);
        v.setName("name");
        v.setAmount(new Money(11.11));
        v.setDate1(new Date());
        v.setDate2(new Date());
        v.setDate3(new Date());
        v.setDate4(new Date());
        v.setDate5(new Date());
        v.setMemo("123456199901011234");
        v.setPhone("123456199901011234");
        v.setGid("123456199901011234");

        v.setDesc1("123456199901011234");
        v.setDesc2("123456199901011234");
        v.setDesc3("123456199901011234");
        v.setDesc4("123456199901011234");
        v.setDesc5("123456199901011234");
        v.setDesc6("123456199901011234");
        String json = MaskJacksonUtils.toJson(v);
        log.info("{}", json);
        log.info("{}", JsonUtils.toPrettyFormat(json));
    }

}
