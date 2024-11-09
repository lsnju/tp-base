package com.lsnju.base.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    @Test
    void test_002() {
        log.info("{}", TpDateUtils.beginOfYesterday());
        log.info("{}", format(TpDateUtils.beginOfYesterday()));

        log.info("{}", TpDateUtils.beginOfTomorrow());
        log.info("{}", format(TpDateUtils.beginOfTomorrow()));
    }

    private static String format(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private static String format(Date date) {
        return TpDateFormatUtils.getNewFormatDateString(date);
    }

    @Test
    void test_parseToZonedDateTime() {
        log.info("----------------");
        log.info("{}", format(TpDateUtils.parseToZonedDateTime("2024-11-01T12:15:17.946+08:00")));
        log.info("{}", format(TpDateUtils.parseToZonedDateTime("2024-11-01T12:15:17+08:00")));
        log.info("{}", format(TpDateUtils.parseToZonedDateTime("2024-11-01T12:15+08:00")));
        log.info("----------------");
        log.info("{}", format(TpDateUtils.parseToZonedDateTime("2024-11-01T12:15:17.946")));
        log.info("{}", format(TpDateUtils.parseToZonedDateTime("2024-11-01T12:15:17")));
        log.info("{}", format(TpDateUtils.parseToZonedDateTime("2024-11-01T12:15")));
        log.info("----------------");
        log.info("{}", format(TpDateUtils.parseToZonedDateTime("2024-11-01T04:15:17.946Z")));
        log.info("----------------");
    }

    @Test
    void test_parseToDate() {
        log.info("----------------");
        log.info("{}", format(TpDateUtils.parseToDate("2024-11-01T12:15:17.946+08:00")));
        log.info("{}", format(TpDateUtils.parseToDate("2024-11-01T12:15:17+08:00")));
        log.info("{}", format(TpDateUtils.parseToDate("2024-11-01T12:15+08:00")));
        log.info("----------------");
        log.info("{}", format(TpDateUtils.parseToDate("2024-11-01T12:15:17.946")));
        log.info("{}", format(TpDateUtils.parseToDate("2024-11-01T12:15:17")));
        log.info("{}", format(TpDateUtils.parseToDate("2024-11-01T12:15")));
        log.info("----------------");
        log.info("{}", format(TpDateUtils.parseToDate("2024-11-01T04:15:17.946Z")));
        log.info("----------------");
    }

}
