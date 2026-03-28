package com.lsnju.base.util;

import org.junit.jupiter.api.Test;

import com.lsnju.base.money.Money;
import com.lsnju.base.util.vo.TestBean;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/3/6 10:56
 * @version V1.0
 */
@Slf4j
public class XmlUtilsFunTest {

    @Test
    void test_001() {
        TestBean obj = new TestBean();
        obj.setId(11);
        obj.setName("name");
        obj.setMemo("memo");
        obj.setAmount(new Money());
        String xml = XmlUtils.toXml(obj);
        log.info("{}", xml);
        log.info("{}", XmlUtils.trim(xml));
        log.info("{}", XmlUtils.simpleFormat(xml));
        log.info("{}", XmlUtils.trimFormat(xml));
    }

}
