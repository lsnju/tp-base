package com.lsnju.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.PropertyMapper;

import com.lsnju.base.money.Money;
import com.lsnju.base.util.TestBean;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/6/15 11:58
 * @version V1.0
 */
@Slf4j
public class PropertyMapperTest {

    @Test
    void test_001() {
        TestBean from1 = new TestBean();
        from1.setAmount(new Money("11.11"));
        TestBean from2 = new TestBean();
        TestBean to1 = new TestBean();
        TestBean to2 = new TestBean();
        PropertyMapper mapper = PropertyMapper.get();
        mapper.from(from1::getAmount).whenNonNull().to(to1::setAmount);
        mapper.from(from2::getAmount).whenNonNull().to(to2::setAmount);
        log.info("{}", to1);
        log.info("{}", to2);
        Assertions.assertNotNull(to1.getAmount());
        Assertions.assertNull(to2.getAmount());
    }

}
