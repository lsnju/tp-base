package com.lsnju.base.util;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023/7/20 20:14
 * @version V1.0
 */
@Slf4j
public class TpDateUtilsTest {

    @Test
    void test_dayOfBegin() {
        Date now = new Date();
        Date expected = TpDateFormatUtils.parseDateWebString(TpDateFormatUtils.getWebDateString(now));
        log.info("{}", TpDateUtils.dayOfBegin(now));
        log.info("{}", expected);
        Assertions.assertEquals(expected, TpDateUtils.dayOfBegin(now));
    }

}
